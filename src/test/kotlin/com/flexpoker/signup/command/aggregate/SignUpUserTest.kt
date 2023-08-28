package com.flexpoker.signup.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.signup.SignUpUser
import com.flexpoker.util.encodePassword
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class SignUpUserTest {

    companion object {
        private const val VALID_EMAIL_ADDRESS = "unique@test.com"
        private const val VALID_USERNAME = "unique"
        private const val VALID_PASSWORD = "123456"
        private val VALID_ENCRYPTED_PASSWORD = encodePassword(VALID_PASSWORD)
        private val VALID_AGGREGATE_ID = UUID.randomUUID()
        private val VALID_SIGN_UP_CODE = UUID.randomUUID()
    }

    @Test
    fun testConfirmSignedUpUserSucceedsEvent() {
        val signUpUser = SignUpUser(
            VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
            VALID_USERNAME, VALID_ENCRYPTED_PASSWORD
        )
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE)
        assertTrue(signUpUser.isConfirmed)
    }

    @Test
    fun testConfirmSignedUpUserFailsBadUsername() {
        val signUpUser = SignUpUser(
            VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
            VALID_USERNAME, VALID_ENCRYPTED_PASSWORD
        )
        assertThrows(FlexPokerException::class.java) {
            signUpUser.confirmSignedUpUser("notequalusername", VALID_SIGN_UP_CODE)
        }
    }

    @Test
    fun testConfirmSignedUpUserFailsBadSignUpCode() {
        val signUpUser = SignUpUser(
            VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
            VALID_USERNAME, VALID_ENCRYPTED_PASSWORD
        )
        assertThrows(FlexPokerException::class.java) {
            signUpUser.confirmSignedUpUser(VALID_USERNAME, UUID.randomUUID())
        }
    }

    @Test
    fun testConfirmSignedUpUserFailedWhenEventAlreadyApplied() {
        val signUpUser = SignUpUser(
            VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
            VALID_USERNAME, VALID_ENCRYPTED_PASSWORD
        )
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE)
        assertThrows(IllegalStateException::class.java) {
            signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE)
        }
    }

}