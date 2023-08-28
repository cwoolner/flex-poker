package com.flexpoker.signup.repository

import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemorySignUpRepositoryTest {

    @Autowired
    private lateinit var repository: SignUpRepository

    @Test
    fun testUsernameExists() {
        sharedTestUsernameExists(repository)
    }

    @Test
    fun testFetchSignUpUser() {
        sharedTestFetchSignUpUser(repository)
    }

    @Test
    fun testSaveSignUpUser() {
        sharedTestSaveSignUpUser(repository)
    }

    @Test
    fun testFindSignUpCodeByUsername() {
        sharedTestFindSignUpCodeByUsername(repository)
    }

    @Test
    fun testFindAggregateIdByUsernameAndSignUpCode() {
        sharedTestFindAggregateIdByUsernameAndSignUpCode(repository)
    }

    @Test
    fun testStoreNewlyConfirmedUsername() {
        sharedTestStoreNewlyConfirmedUsername(repository)
    }

    @Test
    fun testSignUpCodeExists() {
        sharedTestSignUpCodeExists(repository)
    }

    @Test
    fun testStoreSignUpInformation() {
        sharedTestStoreSignUpInformation(repository)
    }

}