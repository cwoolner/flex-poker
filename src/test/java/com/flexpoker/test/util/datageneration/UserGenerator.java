package com.flexpoker.test.util.datageneration;

import com.flexpoker.model.User;

public class UserGenerator {

    public User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }
    
}
