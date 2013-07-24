package com.flexpoker.bso.mock;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.UserBso;

@Service
public class UserBsoMock implements UserBso {

    @Override
    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException, DataAccessException {
        // TODO Auto-generated method stub
        return null;
    }

}
