package com.flexpoker.controller;

import org.springframework.flex.messaging.AsyncMessageCreator;
import org.springframework.flex.messaging.MessageTemplate;
import org.springframework.stereotype.Controller;

import com.flexpoker.model.Game;
import com.flexpoker.model.User;

import flex.messaging.messages.AsyncMessage;

@Controller("eventManager")
public class EventManagerImpl implements EventManager {

    private static final String GAMES_UPDATED = "gamesUpdated";

    private static final String USER_JOINED_GAME = "userJoinedGame";
    
    private static final String GAME_STATUS_UPDATES = "gameStatusUpdates";

    private MessageTemplate messageTemplate;

    @Override
    public void sendGamesUpdatedEvent() {
        messageTemplate.send(GAMES_UPDATED, null);
    }

    @Override
    public void sendUserJoinedEvent(final User user, final Game game) {
        messageTemplate.send(new AsyncMessageCreator() {
            @Override
            public AsyncMessage createMessage() {
                AsyncMessage message = new AsyncMessage();
                message.setDestination(GAME_STATUS_UPDATES);
                message.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME,
                        game.getId() + "." + USER_JOINED_GAME);
               message.setBody(user);
               return message;
            }
        });
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

}
