package com.flexpoker.dao;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import com.flexpoker.dao.api.UserDao;
import com.flexpoker.model.User;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;
    
    @Inject
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Property.forName("username").eq(username));
        return (User) criteria.uniqueResult();
    }

}
