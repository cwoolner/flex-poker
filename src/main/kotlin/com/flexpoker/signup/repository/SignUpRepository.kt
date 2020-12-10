package com.flexpoker.signup.repository

import com.flexpoker.signup.SignUpUser
import java.util.UUID

interface SignUpRepository {
    fun usernameExists(username: String): Boolean
    fun signUpCodeExists(signUpCode: UUID): Boolean
    fun findAggregateIdByUsernameAndSignUpCode(username: String, signUpCode: UUID): UUID?
    fun findSignUpCodeByUsername(username: String): UUID
    fun storeSignUpInformation(aggregateId: UUID, username: String, signUpCode: UUID)
    fun storeNewlyConfirmedUsername(username: String)
    fun fetchSignUpUser(signUpUserId: UUID): SignUpUser?
    fun saveSignUpUser(signUpUser: SignUpUser)
}