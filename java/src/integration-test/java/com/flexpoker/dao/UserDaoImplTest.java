package com.flexpoker.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.Context;


public class UserDaoImplTest {

    private UserDao userDao = (UserDao) Context.instance().getBean("userDao");

    @Test
    public void testFindByUsername() {
        assertTrue(userDao.findByUsername("bogus").isEmpty());
        assertFalse(userDao.findByUsername("john").isEmpty());
    }

}
