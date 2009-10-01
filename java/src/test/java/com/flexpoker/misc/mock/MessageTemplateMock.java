package com.flexpoker.misc.mock;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.flex.messaging.AsyncMessageCreator;
import org.springframework.flex.messaging.MessageTemplate;

import flex.messaging.messages.AsyncMessage;

/**
 * Mock for Spring's {@link MessageTemplate}
 *
 * @author cwoolner
 */
public class MessageTemplateMock extends MessageTemplate {

    @Override
    public AsyncMessage createMessageForDestination(String destination) {
        AsyncMessage asyncMessage = new AsyncMessage();
        asyncMessage.setDestination(destination);
        return asyncMessage;
    }

    @Override
    public void send(AsyncMessageCreator creator) {
        if (!creator.getClass().getSimpleName().equals("GameStatusMessageCreator")
                && !creator.getClass().getSimpleName().equals("TableStatusMessageCreator")) {
            throw new IllegalArgumentException("The creator argument must be "
                    + "either a GameStatusMessageCreator or "
                    + "TableStatusMessageCreator.");
        }
    }

    @Override
    public void send(String destination, Object body) {
        if (!destination.equals("gamesUpdated")
                && !destination.equals("jms-chat")) {
            throw new IllegalArgumentException("Only gamesUpdated and jms-chat "
                    + "are valid destinations.");
        }

        if (destination.equals("gamesUpdated") && body != null) {
            throw new IllegalArgumentException("The gamesUpdated message "
                    + "should not have a body.");
        }

        try {
            if (destination.equals("jms-chat") && (
                    !(body instanceof MapMessage)
                    || StringUtils.isBlank(((MapMessage) body).getString("userId"))
                    || StringUtils.isBlank(((MapMessage) body).getString("chatMessage"))
                    ))
            {
                throw new IllegalArgumentException("The jms-chat message "
                        + "should be a MapMessage with userId and chatMessage "
                        + "values set.");
            }
        } catch (JMSException e) {
            throw new RuntimeException("JMSException was thrown.", e);
        }
    }

}
