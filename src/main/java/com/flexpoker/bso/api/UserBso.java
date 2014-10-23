package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;

import com.flexpoker.model.OpenGameForUser;

public interface UserBso {

    List<OpenGameForUser> fetchUsersOpenGames(Principal principal);

}
