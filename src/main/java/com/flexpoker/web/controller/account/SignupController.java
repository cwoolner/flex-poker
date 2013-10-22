package com.flexpoker.web.controller.account;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.flexpoker.bso.api.UserBso;
import com.flexpoker.dto.Result;
import com.flexpoker.model.User;
import com.flexpoker.web.model.SignupConfirmViewModel;
import com.flexpoker.web.model.SignupViewModel;
import com.flexpoker.web.translator.SignupTranslator;

@Controller
public class SignupController {

    private final UserBso userBso;
    
    @Inject    
    public SignupController(UserBso userBso) {
        this.userBso = userBso;
    }
    
    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String index() {
        return "signup";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView signup(SignupViewModel signupViewModel, ModelAndView modelAndView) {
        User signupUser = new SignupTranslator().translate(signupViewModel);
        userBso.signup(signupUser);
        modelAndView.setViewName("signup-success");
        modelAndView.addObject("email", signupViewModel.getEmail());
        return modelAndView;
    }
    
    @RequestMapping(value = "/signupconfirm", method = RequestMethod.GET)
    public String confirmSignup(SignupConfirmViewModel viewModel, Model model) {
        Result result = userBso.confirmSignup(viewModel.getUsername(), viewModel.getSignupCode());
        
        if (result.hasErrors()) {
            model.addAttribute("error", result.getErrors().get(0));
        }
        
        return "signupconfirm";
    }

}
