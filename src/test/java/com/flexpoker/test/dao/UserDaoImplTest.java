package com.flexpoker.test.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.dao.UserDaoImpl;

public class UserDaoImplTest {

    private UserDaoImpl dao = new UserDaoImpl();

    @Test
    public void testFindByUsername() {
        assertTrue(dao.findByUsername("bogus").isEmpty());
        assertFalse(dao.findByUsername("john").isEmpty());
    }

}
