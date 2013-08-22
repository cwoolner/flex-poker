package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import com.flexpoker.dto.Result;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.model.User;

public interface UserBso {

    List<OpenGameForUser> fetchUsersOpenGames(Principal principal);

    void signup(User signupUser);

    Result confirmSignup(String username, UUID signupCode);

}
