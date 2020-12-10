package com.flexpoker.signup

import com.flexpoker.exception.FlexPokerException
import java.util.UUID

class SignUpUser(val aggregateId: UUID, val signUpCode: UUID, val email: String,
                 val username: String?, val encryptedPassword: String) {

    var isConfirmed = false

    fun confirmSignedUpUser(username: String, signUpCode: UUID) {
        checkNotNull(this.username) { "username should be set already" }
        check(!isConfirmed) { "confirmed should be false" }
        if (this.username != username) {
            throw FlexPokerException("username does not match")
        }
        if (this.signUpCode != signUpCode) {
            throw FlexPokerException("sign-up code does not match")
        }
        isConfirmed = true
    }

}