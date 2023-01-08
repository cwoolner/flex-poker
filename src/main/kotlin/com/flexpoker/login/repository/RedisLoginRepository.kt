package com.flexpoker.login.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.util.PasswordUtils
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID
import javax.annotation.PostConstruct
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.LOGIN_REDIS)
@Repository
class RedisLoginRepository @Inject constructor(private val redisTemplate: RedisTemplate<String, String?>) :
    LoginRepository {

    companion object {
        private const val LOGIN_PASSWORD_NAMESPACE = "login-password:"
        private const val LOGIN_ID_NAMESPACE = "login-id:"
        private const val AGGREGATE_ID_USERNAME_NAMESPACE = "aggregateid-username:"
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return Optional.ofNullable(redisTemplate.opsForValue()[LOGIN_PASSWORD_NAMESPACE + username])
            .map { password: String? -> User(username, password, setOf(SimpleGrantedAuthority("ROLE_USER"))) }
            .orElseThrow { UsernameNotFoundException(username) }
    }

    override fun saveUsernameAndPassword(username: String, encryptedPassword: String) {
        redisTemplate.opsForValue()[LOGIN_PASSWORD_NAMESPACE + username] = encryptedPassword
    }

    override fun fetchAggregateIdByUsername(username: String): UUID {
        val stringAggregateId = redisTemplate.opsForValue()[LOGIN_ID_NAMESPACE + username]
        return UUID.fromString(stringAggregateId)
    }

    override fun saveAggregateIdAndUsername(aggregateId: UUID, username: String) {
        redisTemplate.opsForValue()[LOGIN_ID_NAMESPACE + username] = aggregateId.toString()
        redisTemplate.opsForValue()[AGGREGATE_ID_USERNAME_NAMESPACE + aggregateId] = username
    }

    override fun fetchUsernameByAggregateId(aggregateId: UUID): String {
        return redisTemplate.opsForValue()[AGGREGATE_ID_USERNAME_NAMESPACE + aggregateId]!!
    }

    @PostConstruct
    private fun addDefaultUsers() {
        addUserIfDoesNotExist("player1")
        addUserIfDoesNotExist("player2")
        addUserIfDoesNotExist("player3")
        addUserIfDoesNotExist("player4")
    }

    private fun addUserIfDoesNotExist(username: String) {
        if (!redisTemplate.hasKey(LOGIN_PASSWORD_NAMESPACE + username)) {
            saveUsernameAndPassword(username, PasswordUtils.encode(username))
            saveAggregateIdAndUsername(UUID.randomUUID(), username)
        }
    }

}