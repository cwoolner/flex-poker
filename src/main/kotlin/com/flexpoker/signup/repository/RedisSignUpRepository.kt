package com.flexpoker.signup.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.signup.SignUpUser
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.SessionCallback
import org.springframework.stereotype.Repository
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.SIGNUP_REDIS)
@Repository
class RedisSignUpRepository @Inject constructor(private val redisTemplate: RedisTemplate<String, String>) :
    SignUpRepository {

    companion object {
        private const val EXPIRATION_IN_MINUTES = 10
        private const val SIGN_UP_NAMESPACE = "signup:"
        private const val EXISTING_USERNAME_KEY = (SIGN_UP_NAMESPACE + "existingusernames")
        private const val SIGN_UP_CODE_NAMESPACE = (SIGN_UP_NAMESPACE + "signupcode:")
        private const val SIGN_UP_USER_NAMESPACE = (SIGN_UP_NAMESPACE + "signupuser:")
    }

    override fun usernameExists(username: String): Boolean {
        return redisTemplate.opsForSet().isMember(EXISTING_USERNAME_KEY, username)!!
    }

    override fun signUpCodeExists(signUpCode: UUID): Boolean {
        val signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString()
        return redisTemplate.opsForHash<Any, Any>().hasKey(signUpCodeKey, "username")
    }

    override fun findAggregateIdByUsernameAndSignUpCode(username: String, signUpCode: UUID): UUID? {
        val signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString()
        val objectFromQuery = redisTemplate.opsForHash<Any, Any>()[signUpCodeKey, "username"] ?: return null
        val usernameFromQuery = objectFromQuery as String
        return if (usernameFromQuery == username) {
            UUID.fromString(
                redisTemplate.opsForHash<Any, Any>()[signUpCodeKey, "aggregateid"] as String
            )
        } else null
    }

    override fun storeSignUpInformation(aggregateId: UUID, username: String, signUpCode: UUID) {
        val signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString()
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
        val foundSignUpCodeKey = redisTemplate.execute(RedisCallback { connection ->
            try {
                connection.scan(ScanOptions.scanOptions().match("signup:signupcode*").build()).use { cursor ->
                    while (cursor.hasNext()) {
                        val key = String(cursor.next(), charset("UTF-8"))
                        val usernameFromRedis = redisTemplate.opsForHash<Any, Any>()[key, "username"] as String
                        if (username == usernameFromRedis) {
                            return@RedisCallback key
                        }
                    }
                }
            } catch (e: IOException) {
                throw FlexPokerException("error in Redis")
            }
            throw FlexPokerException("could not find username in Redis")
        })
        return UUID.fromString(foundSignUpCodeKey.split(":").toTypedArray()[2])
    }

    @PostConstruct
    private fun addDefaultSignUps() {
        storeNewlyConfirmedUsername("player1")
        storeNewlyConfirmedUsername("player2")
        storeNewlyConfirmedUsername("player3")
        storeNewlyConfirmedUsername("player4")
    }

    override fun fetchSignUpUser(signUpUserId: UUID): SignUpUser? {
        val signUpCodeKey = SIGN_UP_USER_NAMESPACE + signUpUserId
        val signUpUserObject = redisTemplate.opsForHash<Any, Any>().entries(signUpCodeKey) ?: return null
        return SignUpUser(
            UUID.fromString(signUpUserObject["aggregateId"].toString()),
            UUID.fromString(signUpUserObject["signUpCode"].toString()),
            signUpUserObject["email"].toString(),
            signUpUserObject["username"].toString(),
            signUpUserObject["encryptedPassword"].toString()
        )
    }

    override fun saveSignUpUser(signUpUser: SignUpUser) {
        val signUpUserKey = SIGN_UP_USER_NAMESPACE + signUpUser.aggregateId
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

}