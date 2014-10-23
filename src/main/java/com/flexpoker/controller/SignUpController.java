package com.flexpoker.controller;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.signup.command.commands.ConfirmSignUpUserEmailCommand;
import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.framework.SignUpCommandType;
import com.flexpoker.signup.query.repository.SignUpRepository;

@Controller
public class SignUpController {

    private final CommandPublisher<SignUpCommandType> commandPublisher;

    private final SignUpRepository signUpCodeRepository;

    @Inject
    public SignUpController(CommandPublisher<SignUpCommandType> commandPublisher,
            SignUpRepository signUpCodeRepository) {
        this.commandPublisher = commandPublisher;
        this.signUpCodeRepository = signUpCodeRepository;
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String index() {
        return "sign-up-step1";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ModelAndView signUpStep1Post(String username, String emailAddress,
            String password, ModelAndView modelAndView) {
        boolean usernameExists = signUpCodeRepository.usernameExists(username);

        if (usernameExists) {
            // TODO: show error for existing username
        } else {
            SignUpNewUserCommand command = new SignUpNewUserCommand(username,
                    emailAddress, password);
            commandPublisher.publish(command);
            modelAndView.setViewName("sign-up-step1-success");
            modelAndView.addObject("email", emailAddress);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/sign-up-confirm", method = RequestMethod.GET)
    public ModelAndView signUpStep2Get(@RequestParam UUID signUpCode,
            ModelAndView modelAndView) {
        boolean signUpCodeExists = signUpCodeRepository.signUpCodeExists(signUpCode);

        modelAndView.setViewName("sign-up-step2");

        if (signUpCodeExists) {
            modelAndView.addObject("signUpCode", signUpCode);
        } else {
            // TODO: show error for invalid sign-up code
        }

        return modelAndView;
    }

    @RequestMapping(value = "/sign-up-confirm", method = RequestMethod.POST)
    public ModelAndView signUpStep2Post(String username, @RequestParam UUID signUpCode,
            ModelAndView modelAndView) {
        UUID aggregateId = signUpCodeRepository.findAggregateIdByUsernameAndSignUpCode(
                username, signUpCode);

        if (aggregateId == null) {
            // TODO: show error for invalid sign-up code/username combo
        } else {
            ConfirmSignUpUserEmailCommand command = new ConfirmSignUpUserEmailCommand(
                    aggregateId, username, signUpCode);
            commandPublisher.publish(command);
            modelAndView.setViewName("sign-up-step2-success");
            modelAndView.addObject("username", username);
        }

        return modelAndView;
    }

}
