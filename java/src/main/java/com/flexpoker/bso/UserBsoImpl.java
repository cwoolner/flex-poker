package com.flexpoker.bso;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.UserDao;
import com.flexpoker.model.User;

@Service(value = "userBso")
@Transactional
public class UserBsoImpl implements UserBso {

    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        List<User> userList = userDao.findByUsername(username);

        if (CollectionUtils.isEmpty(userList)) {
            throw new UsernameNotFoundException("Bad credentials.");
        }

        return userList.get(0);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
