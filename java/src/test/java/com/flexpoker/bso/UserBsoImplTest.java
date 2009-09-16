package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.flexpoker.util.Context;

/**
 * Test for UserBsoImpl class.
 *
 * NOTE: The interface is used in defining the dao field due to the
 *       Transactional annotation, which creates a Proxy object.  This Proxy
 *       object is not allowed to be cast.
 *
 * @author chris
 */
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
