package com.flexpoker.dao.api;

import com.flexpoker.model.User;

public interface UserDao {

    User findByUsername(String username);

}
