package com.flexpoker.bso;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.UserBso;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.repository.api.OpenGameForUserRepository;
import com.flexpoker.repository.api.UserRepository;

@Service
public class UserBsoImpl implements UserBso {

    private final UserRepository userDao;

    private final OpenGameForUserRepository openGamesForUserRepository;

    @Inject
    public UserBsoImpl(UserRepository userDao,
            OpenGameForUserRepository openGamesForUserRepository) {
        this.userDao = userDao;
        this.openGamesForUserRepository = openGamesForUserRepository;
    }

    @Override
    public List<OpenGameForUser> fetchUsersOpenGames(Principal principal) {
        return openGamesForUserRepository.fetchAllOpenGamesForUser(principal.getName());
    }

}
