package com.flexpoker.signup.repository

import com.flexpoker.signup.SignUpUser
import com.flexpoker.util.encodePassword
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.UUID

fun sharedTestUsernameExists(repository: SignUpRepository) {
    assertTrue(repository.usernameExists("player1"))
    assertFalse(repository.usernameExists("player1234"))
}

fun sharedTestFetchSignUpUser(repository: SignUpRepository) {
    assertNull(repository.fetchSignUpUser(UUID.randomUUID()))

    val signUpCode = repository.findSignUpCodeByUsername("player1")
    val id = repository.findAggregateIdByUsernameAndSignUpCode("player1", signUpCode)!!
    assertEquals("player1", repository.fetchSignUpUser(id)!!.username)
}

fun sharedTestSaveSignUpUser(repository: SignUpRepository) {
    val id = UUID.randomUUID()
    val signUpCode = UUID.randomUUID()
    val signUpUser = SignUpUser(id, signUpCode, "test@test.com", "player7", encodePassword("test"))
    repository.saveSignUpUser(signUpUser)
    assertEquals("player7", repository.fetchSignUpUser(id)!!.username)
}

fun sharedTestFindSignUpCodeByUsername(repository: SignUpRepository) {
    val signUpCode = repository.findSignUpCodeByUsername("player1")
    assertNotNull(signUpCode)
}

fun sharedTestFindAggregateIdByUsernameAndSignUpCode(repository: SignUpRepository) {
    val signUpCode = repository.findSignUpCodeByUsername("player1")
    val id = repository.findAggregateIdByUsernameAndSignUpCode("player1", signUpCode)
    assertNotNull(id)
}

fun sharedTestStoreNewlyConfirmedUsername(repository: SignUpRepository) {
    repository.storeNewlyConfirmedUsername("player5")
    assertTrue(repository.usernameExists("player5"))
}

fun sharedTestSignUpCodeExists(repository: SignUpRepository) {
    val signUpCode = repository.findSignUpCodeByUsername("player1")
    assertTrue(repository.signUpCodeExists(signUpCode))
    assertFalse(repository.signUpCodeExists(UUID.randomUUID()))
}

fun sharedTestStoreSignUpInformation(repository: SignUpRepository) {
    val id = UUID.randomUUID()
    val signUpCode = UUID.randomUUID()
    repository.storeSignUpInformation(id, "player6", signUpCode)
    assertTrue(repository.signUpCodeExists(signUpCode))
}
