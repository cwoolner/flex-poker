package com.flexpoker.login.repository

import com.flexpoker.util.encodePassword
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.util.UUID

fun sharedTestLoadUserByUsername(repository: LoginRepository) {
    val user = repository.loadUserByUsername("player1")
    assertEquals("player1", user.username)
}

fun sharedTestFetchAggregateIdByUsername(repository: LoginRepository) {
    val userId = repository.fetchAggregateIdByUsername("player1")
    assertNotNull(userId)
}

fun sharedTestFetchUsernameByAggregateId(repository: LoginRepository) {
    val userId = repository.fetchAggregateIdByUsername("player1")
    val username = repository.fetchUsernameByAggregateId(userId)
    assertEquals("player1", username)
}

fun sharedTestSaveAggregateIdAndUsername(repository: LoginRepository) {
    val userId = UUID.randomUUID()
    repository.saveAggregateIdAndUsername(userId, "player5")
    val savedUserId = repository.fetchAggregateIdByUsername("player5")
    assertEquals(userId, savedUserId)
}

fun sharedTestSaveUsernameAndPassword(repository: LoginRepository) {
    repository.saveUsernameAndPassword("player5", encodePassword("password5"))
    assertNotNull(repository.loadUserByUsername("player5"))
}
