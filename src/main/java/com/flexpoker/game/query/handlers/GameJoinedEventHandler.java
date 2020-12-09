package com.flexpoker.game.query.handlers;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.GamePlayerRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class GameJoinedEventHandler implements EventHandler<GameJoinedEvent> {

    private final GameListRepository gameListRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final GamePlayerRepository gamePlayerRepository;

    private final LoginRepository loginRepository;

    private final ChatService chatService;

    @Inject
    public GameJoinedEventHandler(
            GameListRepository gameListRepository,
            PushNotificationPublisher pushNotificationPublisher,
            OpenGameForPlayerRepository openGameForUserRepository,
            GamePlayerRepository gamePlayerRepository,
            LoginRepository loginRepository,
            ChatService chatService) {
        this.gameListRepository = gameListRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.openGameForUserRepository = openGameForUserRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.loginRepository = loginRepository;
        this.chatService = chatService;
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
        var gameName = gameListRepository.fetchGameName(event.getAggregateId());
        openGameForUserRepository.addOpenGameForUser(event.getPlayerId(), event.getAggregateId(), gameName);
    }

    private void handlePushNotifications(GameJoinedEvent event) {
        pushNotificationPublisher.publish(new OpenGamesForPlayerUpdatedPushNotification(
                event.getPlayerId()));
        pushNotificationPublisher.publish(GameListUpdatedPushNotification.INSTANCE);
    }

    private void handleChat(GameJoinedEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " has joined the game";
        chatService.saveAndPushSystemGameChatMessage(event.getAggregateId(), message);
    }

}
