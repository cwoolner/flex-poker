package com.flexpoker.signup.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.signup.SignUpUser
import com.flexpoker.util.encodePassword
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.SessionCallback
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.SIGNUP_REDIS)
@Repository
class RedisSignUpRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, String>,
) : SignUpRepository {

    companion object {
        private const val EXPIRATION_IN_MINUTES = 10
        private const val EXISTING_USERNAME_KEY = "signup:existingusernames"
        private const val SIGN_UP_CODE_NAMESPACE = "signup:signupcode"
        private const val SIGN_UP_USER_NAMESPACE = "signup:signupuser"

        private val SIGNUP_CODE_SCAN_OPTIONS = ScanOptions.scanOptions()
            .match("$SIGN_UP_CODE_NAMESPACE:*")
            .count(100)
            .build()
    }

    private fun signUpCodeRedisKey(signUpCode: UUID) = "$SIGN_UP_CODE_NAMESPACE:$signUpCode"
    private fun signUpUserRedisKey(aggregateId: UUID) = "$SIGN_UP_USER_NAMESPACE:$aggregateId"

    override fun usernameExists(username: String): Boolean {
        return redisTemplate.opsForSet().isMember(EXISTING_USERNAME_KEY, username)!!
    }

    override fun signUpCodeExists(signUpCode: UUID): Boolean {
        val signUpCodeKey = signUpCodeRedisKey(signUpCode)
        return redisTemplate.opsForHash<Any, Any>().hasKey(signUpCodeKey, "username")
    }

    override fun findAggregateIdByUsernameAndSignUpCode(username: String, signUpCode: UUID): UUID? {
        val signUpCodeKey = signUpCodeRedisKey(signUpCode)
        val objectFromQuery = redisTemplate.opsForHash<Any, Any>()[signUpCodeKey, "username"] ?: return null
        val usernameFromQuery = objectFromQuery as String
        return if (usernameFromQuery == username) {
            UUID.fromString(
                redisTemplate.opsForHash<Any, Any>()[signUpCodeKey, "aggregateid"] as String
            )
        } else null
    }

    override fun storeSignUpInformation(aggregateId: UUID, username: String, signUpCode: UUID) {
        val signUpCodeKey = signUpCodeRedisKey(signUpCode)
        redisTemplate.execute(object : SessionCallback<List<Any>> {
            override fun <K : Any?, V : Any?> execute(operations: RedisOperations<K, V>): List<Any>? {
                operations.multi()
                operations.opsForHash<String, String>().put(signUpCodeKey as K, "username", username)
                operations.opsForHash<String, String>().put(signUpCodeKey, "aggregateid", aggregateId.toString())
                operations.expire(signUpCodeKey, EXPIRATION_IN_MINUTES.toLong(), TimeUnit.MINUTES)
                return redisTemplate.exec()
            }
        })
    }

    override fun storeNewlyConfirmedUsername(username: String) {
        redisTemplate.opsForSet().add(EXISTING_USERNAME_KEY, username)
    }

    override fun findSignUpCodeByUsername(username: String): UUID {
        val cursor = redisTemplate.scan(SIGNUP_CODE_SCAN_OPTIONS)
        var foundSignUpCodeKey: String? = null

        while (cursor.hasNext() && foundSignUpCodeKey == null) {
            val key = cursor.next()
            val usernameFromRedis = redisTemplate.opsForHash<String, String>()[key, "username"]
            if (username == usernameFromRedis) {
                foundSignUpCodeKey = key
            }
        }

        if (foundSignUpCodeKey == null) {
            throw FlexPokerException("could not find username in Redis")
        }

        return UUID.fromString(foundSignUpCodeKey.split(":").toTypedArray()[2])
    }

    override fun fetchSignUpUser(signUpUserId: UUID): SignUpUser? {
        val signUpCodeKey = signUpUserRedisKey(signUpUserId)
        val signUpUserObject = redisTemplate.opsForHash<Any, Any>().entries(signUpCodeKey)
        return if (signUpUserObject.isEmpty()) {
            null
        } else {
            SignUpUser(
                UUID.fromString(signUpUserObject["aggregateId"].toString()),
                UUID.fromString(signUpUserObject["signUpCode"].toString()),
                signUpUserObject["email"].toString(),
                signUpUserObject["username"].toString(),
                signUpUserObject["encryptedPassword"].toString()
            )
        }
    }

    override fun saveSignUpUser(signUpUser: SignUpUser) {
        val signUpUserKey = signUpUserRedisKey(signUpUser.aggregateId)
        val signUpUserMap = mapOf(
            "username" to signUpUser.username,
            "email" to signUpUser.email,
            "aggregateId" to signUpUser.aggregateId.toString(),
            "signUpCode" to signUpUser.signUpCode.toString(),
            "confirmed" to java.lang.String.valueOf(signUpUser.isConfirmed),
            "encryptedPassword" to signUpUser.encryptedPassword
        )
        redisTemplate.execute(object : SessionCallback<List<Any>> {
            override fun <K : Any?, V : Any?> execute(operations: RedisOperations<K, V>): List<Any>? {
                operations.multi()
                operations.opsForHash<String, Any>().putAll(signUpUserKey as K, signUpUserMap)
                return redisTemplate.exec()
            }
        })
    }

    @PostConstruct
    private fun addDefaultSignUps() {
        addDefaultSignUp("player1")
        addDefaultSignUp("player2")
        addDefaultSignUp("player3")
        addDefaultSignUp("player4")
    }

    private fun addDefaultSignUp(username: String) {
        val id = UUID.randomUUID()
        val signUpCode = UUID.randomUUID()
        storeNewlyConfirmedUsername(username)
        saveSignUpUser(SignUpUser(id, signUpCode, "$username@test.com", username, encodePassword(username)))
        storeSignUpInformation(id, username, signUpCode)
    }

}