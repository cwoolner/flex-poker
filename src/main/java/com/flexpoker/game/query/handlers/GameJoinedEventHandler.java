package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.GamePlayerRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;

@Component
public class GameJoinedEventHandler implements EventHandler<GameJoinedEvent> {

    private final GameListRepository gameListRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final GamePlayerRepository gamePlayerRepository;

    private final SendGameChatMessageCommand sendGameChatMessageCommand;

    private final LoginRepository loginRepository;

    @Inject
    public GameJoinedEventHandler(GameListRepository gameListRepository,
            PushNotificationPublisher pushNotificationPublisher,
            OpenGameForPlayerRepository openGameForUserRepository,
            GamePlayerRepository gamePlayerRepository,
            SendGameChatMessageCommand sendGameChatMessageCommand,
            LoginRepository loginRepository) {
        this.gameListRepository = gameListRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.openGameForUserRepository = openGameForUserRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(GameJoinedEvent event) {
        handleGamePlayerRepository(event);
        handleOpenGameRepository(event);
        handleGameListRepository(event);
        handlePushNotifications(event);
        handleChat(event);
    }

    private void handleGameListRepository(GameJoinedEvent event) {
        gameListRepository.incrementRegisteredPlayers(event.getAggregateId());
    }

    private void handleGamePlayerRepository(GameJoinedEvent event) {
        gamePlayerRepository.addPlayerToGame(event.getPlayerId(), event.getAggregateId());
    }

    private void handleOpenGameRepository(GameJoinedEvent event) {
        String gameName = gameListRepository.fetchGameName(event.getAggregateId());
        OpenGameForUser openGameForUser = new OpenGameForUser(event.getAggregateId(),
                gameName, GameStage.REGISTERING);
        openGameForUserRepository
                .addOpenGameForUser(event.getPlayerId(), openGameForUser);
    }

    private void handlePushNotifications(GameJoinedEvent event) {
        pushNotificationPublisher.publish(new OpenGamesForPlayerUpdatedPushNotification(
                event.getPlayerId()));
        pushNotificationPublisher.publish(new GameListUpdatedPushNotification());
    }

    // TODO: the event handler shouldn't be sending chat commands. this will be
    // addressed in the chat refactoring
    private void handleChat(GameJoinedEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " has joined the game";
        sendGameChatMessageCommand.execute(new GameChatMessage(message, null, true, event
                .getAggregateId()));
    }

}
