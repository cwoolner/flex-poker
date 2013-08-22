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
import com.flexpoker.dto.Result;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.model.User;
import com.flexpoker.repository.api.SignupRepository;

@Service
public class UserBsoImpl implements UserBso {

    private final UserDao userDao;
    
    private final SignupRepository signupRepository;

    @Inject
    public UserBsoImpl(UserDao userDao, SignupRepository signupRepository) {
        this.userDao = userDao;
        this.signupRepository = signupRepository;
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
        
        UUID signupCode = UUID.randomUUID();
        signupRepository.storeSignupCode(signupUser.getUsername(), signupCode, 10);
    }

    @Override
    public Result confirmSignup(String username, UUID signupCode) {
        UUID returnedCode = signupRepository.fetchSignupCode(username);
        
        // code no longer exists or never did exist
        if (returnedCode == null) {
            Result result = new Result();
            result.addError("Username not found.  The time limit of 10 minutes may have been exceeded.");
            return result;
        }
        
        // all good.  enable the user in the database and remove the signup code
        if (returnedCode.equals(signupCode)) {
            signupRepository.removeSignupCode(username);
            User user = userDao.findByUsername(username);
            user.setEnabled(true);
            userDao.updateUser(user);
            return new Result();
        }
        
        // signup code exists, but they do not match
        Result result = new Result();
        result.addError("Invalid signup code.");
        return result;
    }

}
