package com.flexpoker.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.IntegrationContext;


public class UserDaoImplIntTest {

    private UserDao userDao = (UserDao) IntegrationContext.instance().getBean("userDao");

    @Test
    public void testFindByUsername() {
        assertTrue(userDao.findByUsername("bogus").isEmpty());
        assertFalse(userDao.findByUsername("john").isEmpty());
    }

}
