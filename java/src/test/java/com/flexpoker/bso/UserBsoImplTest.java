package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.flexpoker.util.Context;


public class UserBsoImplTest {

    private UserBso bso = (UserBso) Context.instance().getBean("userBso");
    
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
