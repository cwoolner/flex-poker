package com.flexpoker.dao.mock;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.flexpoker.dao.UserDao;
import com.flexpoker.model.User;

@Repository
public class UserDaoMock implements UserDao {

    @Override
    public List<User> findByUsername(String username) {
        if (StringUtils.equals(username, "jgalt")) {
            User user = new User();
            user.setUsername("jgalt");
            
            List<User> users = new ArrayList<User>();
            users.add(user);
            return users;
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User findById(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(User entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Integer id, User entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        // TODO Auto-generated method stub

    }

}
