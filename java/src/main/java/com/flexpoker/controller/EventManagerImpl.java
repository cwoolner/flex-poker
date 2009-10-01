package com.flexpoker.controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.flex.messaging.AsyncMessageCreator;
import org.springframework.flex.messaging.MessageTemplate;
import org.springframework.stereotype.Controller;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

import flex.messaging.messages.AsyncMessage;

@Controller("eventManager")
public class EventManagerImpl implements EventManager {

    private static final String GAMES_UPDATED = "gamesUpdated";

    private static final String USER_JOINED_GAME = "userJoinedGame";

    private static final String GAME_STATUS_UPDATES = "gameStatusUpdates";

    private static final String TABLE_STATUS_UPDATES = "tableStatusUpdates";

    private static final String JMS_CHAT = "jms-chat";

    private static final String GAME_IS_STARTING = "gameIsStarting";

    private static final String GAME_IN_PROGRESS = "gameInProgress";

    private static final String NEW_HAND_STARTING = "newHandStarting";

    private static final String DEAL_FLOP = "dealFlop";

    private static final String DEAL_RIVER = "dealRiver";

    private static final String DEAL_TURN = "dealTurn";

    private static final String HAND_COMPLETE = "handComplete";

    private static final String USER_ACTED = "userActed";

    private MessageTemplate messageTemplate;

    @Override
    public void sendGamesUpdatedEvent() {
        messageTemplate.send(GAMES_UPDATED, null);
    }

    @Override
    public void sendUserJoinedEvent(Game game) {
        messageTemplate.send(new GameStatusMessageCreator(game, USER_JOINED_GAME));
    }

    @Override
    public void sendChatEvent(String username, String text) {
        MapMessage mapMessage = new ActiveMQMapMessage();

        try {
            mapMessage.setString("userId", username);
            mapMessage.setString("chatMessage", text);
        } catch (JMSException e) {
            throw new RuntimeException("JMSException thrown while trying to "
                + "send chat event.");
        }

        messageTemplate.send(JMS_CHAT, mapMessage);
    }

    @Override
    public void sendGameStartingEvent(Game game) {
        messageTemplate.send(new GameStatusMessageCreator(game, GAME_IS_STARTING));
    }

    @Override
    public void sendGameInProgressEvent(Game game) {
        messageTemplate.send(new GameStatusMessageCreator(game, GAME_IN_PROGRESS));
    }

    @Override
    public void sendNewHandStartingEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, NEW_HAND_STARTING));
    }

    @Override
    public void sendDealFlopEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, DEAL_FLOP));
    }

    @Override
    public void sendDealRiverEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, DEAL_RIVER));
    }

    @Override
    public void sendDealTurnEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, DEAL_TURN));
    }

    @Override
    public void sendHandCompleteEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, HAND_COMPLETE));
    }

    @Override
    public void sendUserActedEvent(Game game, Table table) {
        messageTemplate.send(new TableStatusMessageCreator(game, table, USER_ACTED));
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    private abstract class FlexPokerMessageCreator implements AsyncMessageCreator {

        protected String fullSubtopic;

        protected String destination;

        @Override
        public AsyncMessage createMessage() {
            AsyncMessage message = messageTemplate
                    .createMessageForDestination(destination);
            message.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, fullSubtopic);
            return message;
        }

    }

    private class TableStatusMessageCreator extends FlexPokerMessageCreator {

        public TableStatusMessageCreator(Game game, Table table, String subtopic) {
            super();
            if (game == null || game.getId() == null) {
                throw new IllegalArgumentException("Game or game id cannot "
                        + "be null.");
            }
            if (table == null || table.getId() == null) {
                throw new IllegalArgumentException("Table or table id cannot "
                        + "be null.");
            }
            if (StringUtils.isBlank(subtopic)) {
                throw new IllegalArgumentException("Subtopic cannot be blank.");
            }
            fullSubtopic = game.getId() + "." + table.getId() + "." + subtopic;
            destination = TABLE_STATUS_UPDATES;
        }

    }

    private class GameStatusMessageCreator extends FlexPokerMessageCreator {

        public GameStatusMessageCreator(Game game, String subtopic) {
            super();
            if (game == null || game.getId() == null) {
                throw new IllegalArgumentException("Game or game id cannot "
                        + "be null.");
            }
            if (StringUtils.isBlank(subtopic)) {
                throw new IllegalArgumentException("Subtopic cannot be blank.");
            }
            fullSubtopic = game.getId() + "." + subtopic;
            destination = GAME_STATUS_UPDATES;
        }

    }

}
