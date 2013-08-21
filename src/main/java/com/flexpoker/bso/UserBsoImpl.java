package com.flexpoker.bso;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.UserBso;
import com.flexpoker.dao.api.UserDao;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.model.User;

@Service
public class UserBsoImpl implements UserBso {

    private final UserDao userDao;

    @Inject
    public UserBsoImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public List<OpenGameForUser> fetchUsersOpenGames(Principal principal) {
        return Arrays.asList(new OpenGameForUser[] { new OpenGameForUser(
                UUID.randomUUID(), "Game 1", GameStage.REGISTERING)});
    }

    @Override
    public void signup(User signupUser) {
        // TODO: need validation for password and for username/email uniqueness
        signupUser.setEnabled(false);
        signupUser.setPassword(new ShaPasswordEncoder().encodePassword(signupUser.getPassword(), null));
        userDao.insertUser(signupUser);
        userDao.insertAuthority(signupUser.getUsername(), "USER");
    }

}
