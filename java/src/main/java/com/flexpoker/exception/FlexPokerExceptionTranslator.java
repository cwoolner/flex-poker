package com.flexpoker.exception;

import org.springframework.flex.core.ExceptionTranslator;
import org.springframework.stereotype.Service;

import flex.messaging.MessageException;

@Service("flexPokerExceptionTranslator")
public class FlexPokerExceptionTranslator implements ExceptionTranslator {

    @Override
    public boolean handles(Class<?> exceptionClass) {
        return exceptionClass == FlexPokerException.class;
    }

    @Override
    public MessageException translate(Throwable exception) {
        MessageException messageException = new MessageException(exception);
        return messageException;
    }

}
