package com.flexpoker.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlindsTest {

    @Test
    public void testBlinds() {
        Blinds blinds = new Blinds(10, 20);
        assertEquals(10, blinds.getSmallBlind());
        assertEquals(20, blinds.getBigBlind());

        blinds = new Blinds(Integer.MAX_VALUE / 2, Integer.MAX_VALUE - 1);
        assertEquals(Integer.MAX_VALUE / 2, blinds.getSmallBlind());
        assertEquals(Integer.MAX_VALUE - 1, blinds.getBigBlind());

        try { new Blinds(Integer.MAX_VALUE / 2, Integer.MAX_VALUE);
        fail("should have thrown exception"); }
        catch (Exception e) {}

        try { new Blinds((Integer.MAX_VALUE / 2) + 1, Integer.MAX_VALUE);
        fail("should have thrown exception"); }
        catch (Exception e) {}

        try { new Blinds(0, 1); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(1, 0); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(-1, 0); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(0, -1); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(-20, -40); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(-20, 40); fail("should have thrown exception"); }
        catch (Exception e) {}
        try { new Blinds(20, -40); fail("should have thrown exception"); }
        catch (Exception e) {}
    }

}
