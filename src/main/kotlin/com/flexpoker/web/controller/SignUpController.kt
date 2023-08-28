package com.flexpoker.web.controller

import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.signup.SignUpUser
import com.flexpoker.signup.repository.SignUpRepository
import com.flexpoker.util.encodePassword
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.nio.charset.Charset
import java.util.UUID
import javax.inject.Inject

@Controller
class SignUpController @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val loginRepository: LoginRepository) {

    @GetMapping("/sign-up", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun index(): String = ClassPathResource("index.html").getContentAsString(Charset.defaultCharset())

    @PostMapping("/sign-up")
    fun signUpStep1Post(@RequestBody model: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val username = model["username"]!!
        val password = model["password"]!!
        val emailAddress = model["emailAddress"]!!
        val usernameExists = signUpRepository.usernameExists(username)
        if (usernameExists) {
            return ResponseEntity(mapOf("error" to "Username already exists"), HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val aggregateId = UUID.randomUUID()
        val signUpCode = UUID.randomUUID()
        val encryptedPassword = encodePassword(password)
        signUpRepository.saveSignUpUser(
            SignUpUser(aggregateId, signUpCode, emailAddress, username, encryptedPassword)
        )
        signUpRepository.storeSignUpInformation(aggregateId, username, signUpCode)

        // TODO: return a generic success once emails can be sent
        return ResponseEntity(mapOf("email" to emailAddress, "username" to username), HttpStatus.OK)
    }

    @GetMapping("/sign-up-confirm", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun signUpConfirm(): String = ClassPathResource("index.html").getContentAsString(Charset.defaultCharset())

    @GetMapping("/confirm-sign-up")
    fun signUpStep2Get(@RequestParam username: String): ResponseEntity<Map<String, Any>> {
        // TODO: signUpCode should be sent in once email works, username is temporary
        val signUpCode = signUpRepository.findSignUpCodeByUsername(username!!)
        val signUpCodeExists = signUpRepository.signUpCodeExists(signUpCode)
        if (!signUpCodeExists) {
            return ResponseEntity(mapOf("error" to "signup code is invalid"), HttpStatus.UNPROCESSABLE_ENTITY)
        }
        return ResponseEntity(mapOf("signUpCode" to signUpCode), HttpStatus.OK)
    }

    @PostMapping("/sign-up-confirm")
    fun signUpStep2Post(@RequestBody model: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val username = model["username"]!!
        val signUpCode = UUID.fromString(model["signUpCode"]!!)

        val aggregateId = signUpRepository.findAggregateIdByUsernameAndSignUpCode(username, signUpCode)
            ?: return ResponseEntity(mapOf(
                "error" to "Invalid username and sign up code combination",
                "signUpCode" to signUpCode
            ), HttpStatus.UNPROCESSABLE_ENTITY)

        val signUpUser = signUpRepository.fetchSignUpUser(aggregateId)
        signUpUser!!.confirmSignedUpUser(username, signUpCode)
        signUpRepository.saveSignUpUser(signUpUser)
        signUpRepository.storeNewlyConfirmedUsername(username)
        loginRepository.saveUsernameAndPassword(username, signUpUser.encryptedPassword)
        loginRepository.saveAggregateIdAndUsername(aggregateId, username)

        return ResponseEntity(mapOf("username" to username), HttpStatus.OK)
    }

}