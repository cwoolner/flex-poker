package com.flexpoker.web.controller

import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.signup.SignUpUser
import com.flexpoker.signup.repository.SignUpRepository
import com.flexpoker.util.PasswordUtils.encode
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import java.util.UUID
import javax.inject.Inject

@Controller
class SignUpController @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val loginRepository: LoginRepository) {

    @GetMapping("/sign-up")
    fun index(): String {
        return "sign-up-step1"
    }

    @PostMapping("/sign-up")
    fun signUpStep1Post(username: String, emailAddress: String, password: String): ModelAndView {
        val usernameExists = signUpRepository.usernameExists(username)
        if (usernameExists) {
            val modelAndView = ModelAndView("sign-up-step1")
            modelAndView.addObject("error", "Username already exists")
            return modelAndView
        }
        val aggregateId = UUID.randomUUID()
        val signUpCode = UUID.randomUUID()
        val encryptedPassword = encode(password)
        signUpRepository.saveSignUpUser(
            SignUpUser(aggregateId, signUpCode, emailAddress, username, encryptedPassword))
        signUpRepository.storeSignUpInformation(aggregateId, username, signUpCode)
        val modelAndView = ModelAndView("sign-up-step1-success")
        modelAndView.viewName = "sign-up-step1-success"
        modelAndView.addObject("email", emailAddress)
        // TODO: remove this once emails can be sent
        modelAndView.addObject("username", username)
        return modelAndView
    }

    @GetMapping("/sign-up-confirm")
    fun signUpStep2Get(@RequestParam username: String?): ModelAndView {

        // TODO: signUpCode should be sent in once email works, username is temporary
        val signUpCode = signUpRepository.findSignUpCodeByUsername(username!!)
        val signUpCodeExists = signUpRepository.signUpCodeExists(signUpCode)
        val modelAndView = ModelAndView("sign-up-step2")
        if (signUpCodeExists) {
            modelAndView.addObject("signUpCode", signUpCode)
        } else {
            // TODO: show error for invalid sign-up code
        }
        return modelAndView
    }

    @PostMapping("/sign-up-confirm")
    fun signUpStep2Post(username: String, @RequestParam signUpCode: UUID): ModelAndView {
        val aggregateId = signUpRepository.findAggregateIdByUsernameAndSignUpCode(username, signUpCode)
        if (aggregateId == null) {
            val modelAndView = ModelAndView("sign-up-step2")
            modelAndView.addObject("signUpCode", signUpCode)
            modelAndView.addObject("error", "Invalid username and sign up code combination")
            return modelAndView
        }
        val signUpUser = signUpRepository.fetchSignUpUser(aggregateId)
        signUpUser!!.confirmSignedUpUser(username, signUpCode)
        signUpRepository.saveSignUpUser(signUpUser)
        signUpRepository.storeNewlyConfirmedUsername(username)
        loginRepository.saveUsernameAndPassword(username, signUpUser.encryptedPassword)
        loginRepository.saveAggregateIdAndUsername(aggregateId, username)
        val modelAndView = ModelAndView("sign-up-step2-success")
        modelAndView.addObject("username", username)
        return modelAndView
    }

}