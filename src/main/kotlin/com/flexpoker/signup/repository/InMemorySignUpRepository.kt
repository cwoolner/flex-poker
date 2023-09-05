package com.flexpoker.signup.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.signup.SignUpUser
import com.flexpoker.util.encodePassword
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.SIGNUP_INMEMORY)
@Repository
class InMemorySignUpRepository : SignUpRepository {

    private val usernameSet: MutableSet<String> = HashSet()
    private val signUpCodeMap: MutableMap<UUID, MutableMap<String, UUID>> = HashMap()
    private val signUpUserMap: MutableMap<UUID, SignUpUser> = HashMap()

    override fun usernameExists(username: String): Boolean {
        return usernameSet.contains(username)
    }

    override fun signUpCodeExists(signUpCode: UUID): Boolean {
        return signUpCodeMap.containsKey(signUpCode)
    }

    override fun findAggregateIdByUsernameAndSignUpCode(username: String, signUpCode: UUID): UUID? {
        val foundSignUpCode: Map<String, UUID>? = signUpCodeMap[signUpCode]
        return foundSignUpCode?.get(username)
    }

    override fun storeSignUpInformation(aggregateId: UUID, username: String, signUpCode: UUID) {
        signUpCodeMap[signUpCode] = HashMap()
        signUpCodeMap[signUpCode]!![username] = aggregateId
    }

    override fun storeNewlyConfirmedUsername(username: String) {
        usernameSet.add(username)
    }

    override fun findSignUpCodeByUsername(username: String): UUID {
        return signUpCodeMap.entries.stream()
            .filter { x: Map.Entry<UUID, Map<String, UUID>> -> x.value.containsKey(username) }
            .findAny().get().key
    }

    override fun fetchSignUpUser(signUpUserId: UUID): SignUpUser? {
        return signUpUserMap[signUpUserId]
    }

    override fun saveSignUpUser(signUpUser: SignUpUser) {
        signUpUserMap[signUpUser.aggregateId] = signUpUser
    }

    @PostConstruct
    private fun addDefaultSignUps() {
        addDefaultSignUp("player1")
        addDefaultSignUp("player2")
        addDefaultSignUp("player3")
        addDefaultSignUp("player4")
    }

    private fun addDefaultSignUp(username: String) {
        val id = UUID.randomUUID()
        val signUpCode = UUID.randomUUID()
        storeNewlyConfirmedUsername(username)
        saveSignUpUser(SignUpUser(id, signUpCode, "$username@test.com", username, encodePassword(username)))
        storeSignUpInformation(id, username, signUpCode)
    }

}