package com.flexpoker.bso;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.UserBso;
import com.flexpoker.dao.api.UserDao;
import com.flexpoker.model.User;

@Service
public class UserBsoImpl implements UserBso {

    private final UserDao userDao;

    @Inject
    public UserBsoImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Bad credentials.");
        }

        return user;
    }

}
