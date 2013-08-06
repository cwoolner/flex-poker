package com.flexpoker.dao.api;

import java.util.List;

import com.flexpoker.model.User;

public interface UserDao {

    List<User> findByUsername(String username);

}
