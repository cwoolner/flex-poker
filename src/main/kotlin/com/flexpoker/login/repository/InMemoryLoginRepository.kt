package com.flexpoker.login.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.util.encodePassword
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.LOGIN_INMEMORY)
@Repository
class InMemoryLoginRepository : LoginRepository {

    private val loginUserMap: MutableMap<String, UserDetails> = HashMap()
    private val loginIdMap: MutableMap<String, UUID> = HashMap()
    private val aggregateIdUsernameMap: MutableMap<UUID, String> = HashMap()

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return Optional.ofNullable(loginUserMap[username])
            .map { user: UserDetails -> User(user.username, user.password, user.authorities) }
            .orElseThrow { UsernameNotFoundException(username) }
    }

    override fun saveUsernameAndPassword(username: String, encryptedPassword: String) {
        loginUserMap[username] = User(username, encryptedPassword, setOf(SimpleGrantedAuthority("ROLE_USER")))
    }

    override fun fetchAggregateIdByUsername(username: String): UUID {
        return loginIdMap[username]!!
    }

    override fun saveAggregateIdAndUsername(aggregateId: UUID, username: String) {
        loginIdMap[username] = aggregateId
        aggregateIdUsernameMap[aggregateId] = username
    }

    override fun fetchUsernameByAggregateId(aggregateId: UUID): String {
        return aggregateIdUsernameMap[aggregateId]!!
    }

    @PostConstruct
    private fun addDefaultUsers() {
        saveUsernameAndPassword("player1", encodePassword("player1"))
        saveUsernameAndPassword("player2", encodePassword("player2"))
        saveUsernameAndPassword("player3", encodePassword("player3"))
        saveUsernameAndPassword("player4", encodePassword("player4"))
        saveAggregateIdAndUsername(UUID.randomUUID(), "player1")
        saveAggregateIdAndUsername(UUID.randomUUID(), "player2")
        saveAggregateIdAndUsername(UUID.randomUUID(), "player3")
        saveAggregateIdAndUsername(UUID.randomUUID(), "player4")
    }

}
