package com.flexpoker.exception;

import static org.junit.Assert.*;

import org.junit.Test;

import flex.messaging.MessageException;


public class FlexPokerExceptionTranslatorTest {

    private FlexPokerExceptionTranslator flexPokerExceptionTranslator =
            new FlexPokerExceptionTranslator();
    
    @Test
    public void testHandles() {
        assertTrue(flexPokerExceptionTranslator.handles(FlexPokerException.class));
        assertFalse(flexPokerExceptionTranslator.handles(Exception.class));
    }

    @Test
    public void testTranslate() {
        MessageException messageException = flexPokerExceptionTranslator
                .translate(new FlexPokerException("Test"));
        assertTrue(messageException.getRootCause() instanceof FlexPokerException);
        assertTrue(messageException.getRootCause().getMessage().equals("Test"));
    }

}
