package com.flexpoker.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.IntegrationContext;

/**
 * Integration test for UserDaoImpl class.
 *
 * @author chris
 */
public class UserDaoImplIntTest {

    private UserDaoImpl dao = (UserDaoImpl) IntegrationContext.instance()
            .getBean("userDao");

    @Test
    public void testFindByUsername() {
        assertTrue(dao.findByUsername("bogus").isEmpty());
        assertFalse(dao.findByUsername("john").isEmpty());
    }

}
