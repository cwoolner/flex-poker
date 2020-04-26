package com.flexpoker.web.controller;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.signup.SignUpUser;
import com.flexpoker.signup.repository.SignUpRepository;
import com.flexpoker.util.PasswordUtils;

@Controller
public class SignUpController {

    private final SignUpRepository signUpRepository;

    private final LoginRepository loginRepository;

    @Inject
    public SignUpController(SignUpRepository signUpRepository,
            LoginRepository loginRepository) {
        this.signUpRepository = signUpRepository;
        this.loginRepository = loginRepository;
    }

    @GetMapping("/sign-up")
    public String index() {
        return "sign-up-step1";
    }

    @PostMapping("/sign-up")
    public ModelAndView signUpStep1Post(String username, String emailAddress,
            String password) {
        var usernameExists = signUpRepository.usernameExists(username);

        if (usernameExists) {
            var modelAndView = new ModelAndView("sign-up-step1");
            modelAndView.addObject("error", "Username already exists");
            return modelAndView;
        }

        var aggregateId = UUID.randomUUID();
        var signUpCode = UUID.randomUUID();

        var encryptedPassword = PasswordUtils.encode(password);

        signUpRepository.saveSignUpUser(new SignUpUser(aggregateId, signUpCode, emailAddress, username, encryptedPassword));
        signUpRepository.storeSignUpInformation(aggregateId, username, signUpCode);

        var modelAndView = new ModelAndView("sign-up-step1-success");
        modelAndView.setViewName("sign-up-step1-success");
        modelAndView.addObject("email", emailAddress);
        // TODO: remove this once emails can be sent
        modelAndView.addObject("username", username);
        return modelAndView;
    }

    @GetMapping("/sign-up-confirm")
    public ModelAndView signUpStep2Get(@RequestParam String username) {

        // TODO: signUpCode should be sent in once email works, username is temporary
        var signUpCode = signUpRepository.findSignUpCodeByUsername(username);

        var signUpCodeExists = signUpRepository.signUpCodeExists(signUpCode);

        var modelAndView = new ModelAndView("sign-up-step2");

        if (signUpCodeExists) {
            modelAndView.addObject("signUpCode", signUpCode);
        } else {
            // TODO: show error for invalid sign-up code
        }

        return modelAndView;
    }

    @PostMapping("/sign-up-confirm")
    public ModelAndView signUpStep2Post(String username, @RequestParam UUID signUpCode) {
        var aggregateId = signUpRepository.findAggregateIdByUsernameAndSignUpCode(username, signUpCode);

        if (aggregateId == null) {
            var modelAndView = new ModelAndView("sign-up-step2");
            modelAndView.addObject("signUpCode", signUpCode);
            modelAndView.addObject("error", "Invalid username and sign up code combination");
            return modelAndView;
        }

        var signUpUser = signUpRepository.fetchSignUpUser(aggregateId);
        signUpUser.confirmSignedUpUser(username, signUpCode);

        signUpRepository.saveSignUpUser(signUpUser);
        signUpRepository.storeNewlyConfirmedUsername(username);

        loginRepository.saveUsernameAndPassword(username, signUpUser.getEncryptedPassword());
        loginRepository.saveAggregateIdAndUsername(aggregateId, username);

        var modelAndView = new ModelAndView("sign-up-step2-success");
        modelAndView.addObject("username", username);
        return modelAndView;
    }

}
