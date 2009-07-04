package com.flexpoker.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.User;

@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Integer>
        implements UserDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findByUsername(String username) {
        Query query = entityManager.createQuery("from " + User.class.getName()
            + " where username = :username");
        query.setParameter("username", username);
        return query.getResultList();
    }

}
