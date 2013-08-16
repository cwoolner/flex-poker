package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.flexpoker.model.OpenGameForUser;

public interface UserBso extends UserDetailsService {

    List<OpenGameForUser> fetchUsersOpenGames(Principal principal);

}
