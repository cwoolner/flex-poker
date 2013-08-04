package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.flexpoker.dao.UserDaoImpl;

public class UserBsoImplTest {

    private UserBsoImpl bso = new UserBsoImpl(new UserDaoImpl(null));
    
    @Test
    public void testLoadUserByUsername() {
        try {
            assertNull(bso.loadUserByUsername("bogus"));
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {}
        
        try {
            assertNull(bso.loadUserByUsername(""));
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {}

        try {
            assertNull(bso.loadUserByUsername(null));
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {}

        assertEquals("jgalt", bso.loadUserByUsername("jgalt").getUsername());
    }

}
