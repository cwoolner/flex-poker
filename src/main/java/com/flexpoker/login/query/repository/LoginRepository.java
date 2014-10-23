package com.flexpoker.login.query.repository;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginRepository extends UserDetailsService {

    void saveLogin(String username, String password);

}
