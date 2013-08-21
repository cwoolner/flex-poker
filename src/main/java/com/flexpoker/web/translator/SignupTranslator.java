package com.flexpoker.web.translator;

import com.flexpoker.model.User;
import com.flexpoker.web.model.SignupViewModel;

public class SignupTranslator {

    public User translate(SignupViewModel viewModel) {
        User user = new User();
        user.setEmail(viewModel.getEmail());
        user.setPassword(viewModel.getPassword());
        user.setUsername(viewModel.getUsername());
        return user;
    }
}
