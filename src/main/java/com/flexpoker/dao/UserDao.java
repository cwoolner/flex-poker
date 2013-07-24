package com.flexpoker.dao;

import java.util.List;

import com.flexpoker.model.User;

public interface UserDao extends GenericDao<User, Integer> {

    List<User> findByUsername(String username);

}
