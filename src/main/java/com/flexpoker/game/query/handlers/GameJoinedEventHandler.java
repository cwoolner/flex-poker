package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.chat.repository.ChatRepository;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.GamePlayerRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Component
public class GameJoinedEventHandler implements EventHandler<GameJoinedEvent> {

    private final GameListRepository gameListRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final GamePlayerRepository gamePlayerRepository;

    private final LoginRepository loginRepository;

    private final ChatRepository chatRepository;

    @Inject
    public GameJoinedEventHandler(
            GameListRepository gameListRepository,
            PushNotificationPublisher pushNotificationPublisher,
            OpenGameForPlayerRepository openGameForUserRepository,
            GamePlayerRepository gamePlayerRepository,
            LoginRepository loginRepository,
            ChatRepository chatRepository) {
        this.gameListRepository = gameListRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.openGameForUserRepository = openGameForUserRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.loginRepository = loginRepository;
        this.chatRepository = chatRepository;
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
        pushNotificationPublisher.publish(new GameListUpdatedPushNotification());
    }

    private void handleChat(GameJoinedEvent event) {
        var username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        var message = username + " has joined the game";
        chatRepository.saveChatMessage(
                new ChatMessageDTO(event.getAggregateId(), null, message, null, true));
        pushNotificationPublisher.publish(
                new ChatSentPushNotification(event.getAggregateId(), null, message, null, true));
    }

}
