package com.flexpoker.repository.api;

import com.flexpoker.model.User;

public interface UserRepository {

    User findByUsername(String username);

    void insertUser(User signupUser);

    void updateUser(User user);

}
