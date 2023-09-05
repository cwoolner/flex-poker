package com.flexpoker.login.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.util.encodePassword
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.LOGIN_REDIS)
@Repository
class RedisLoginRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, String?>,
) : LoginRepository {

    companion object {
        private const val LOGIN_PASSWORD_NAMESPACE = "login-password"
        private const val LOGIN_ID_NAMESPACE = "login-id"
        private const val AGGREGATE_ID_USERNAME_NAMESPACE = "aggregateid-username"
    }

    private fun loginPasswordRedisKey(username: String) = "$LOGIN_PASSWORD_NAMESPACE:$username"
    private fun loginIdRedisKey(username: String) = "$LOGIN_ID_NAMESPACE:$username"
    private fun aggregateIdUsernameRedisKey(aggregateId: UUID) = "$AGGREGATE_ID_USERNAME_NAMESPACE:$aggregateId"

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return Optional.ofNullable(redisTemplate.opsForValue()[loginPasswordRedisKey(username)])
            .map { password: String? -> User(username, password, setOf(SimpleGrantedAuthority("ROLE_USER"))) }
            .orElseThrow { UsernameNotFoundException(username) }
    }

    override fun saveUsernameAndPassword(username: String, encryptedPassword: String) {
        redisTemplate.opsForValue()[loginPasswordRedisKey(username)] = encryptedPassword
    }

    override fun fetchAggregateIdByUsername(username: String): UUID {
        val stringAggregateId = redisTemplate.opsForValue()[loginIdRedisKey(username)]
        return UUID.fromString(stringAggregateId)
    }

    override fun saveAggregateIdAndUsername(aggregateId: UUID, username: String) {
        redisTemplate.opsForValue()[loginIdRedisKey(username)] = aggregateId.toString()
        redisTemplate.opsForValue()[aggregateIdUsernameRedisKey(aggregateId)] = username
    }

    override fun fetchUsernameByAggregateId(aggregateId: UUID): String {
        return redisTemplate.opsForValue()[aggregateIdUsernameRedisKey(aggregateId)]!!
    }

    @PostConstruct
    private fun addDefaultUsers() {
        addUserIfDoesNotExist("player1")
        addUserIfDoesNotExist("player2")
        addUserIfDoesNotExist("player3")
        addUserIfDoesNotExist("player4")
    }

    private fun addUserIfDoesNotExist(username: String) {
        if (!redisTemplate.hasKey(loginPasswordRedisKey(username))) {
            saveUsernameAndPassword(username, encodePassword(username))
            saveAggregateIdAndUsername(UUID.randomUUID(), username)
        }
    }

}