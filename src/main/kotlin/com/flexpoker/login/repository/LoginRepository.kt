package com.flexpoker.login.repository

import org.springframework.security.core.userdetails.UserDetailsService
import java.util.UUID

interface LoginRepository : UserDetailsService {
    fun saveUsernameAndPassword(username: String, encryptedPassword: String)
    fun fetchAggregateIdByUsername(username: String): UUID
    fun saveAggregateIdAndUsername(aggregateId: UUID, username: String)
    fun fetchUsernameByAggregateId(aggregateId: UUID): String
}