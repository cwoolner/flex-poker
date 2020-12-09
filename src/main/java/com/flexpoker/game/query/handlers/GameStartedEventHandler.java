package com.flexpoker.game.query.handlers;

import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.GamePlayerRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;
import java.util.UUID;

@Component
public class GameStartedEventHandler implements EventHandler<GameStartedEvent> {

    private final GameListRepository gameListRepository;

    private final GamePlayerRepository gamePlayerRepository;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatService chatService;

    @Inject
    public GameStartedEventHandler(
            GameListRepository gameListRepository,
            GamePlayerRepository gamePlayerRepository,
            OpenGameForPlayerRepository openGameForUserRepository,
            PushNotificationPublisher pushNotificationPublisher,
            ChatService chatService) {
        this.gameListRepository = gameListRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.openGameForUserRepository = openGameForUserRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.chatService = chatService;
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        var playerIdsForGame = handleOpenGameRepository(event);
        handleGameListRepository(event);
        handlePushNotifications(playerIdsForGame);
        handleChat(event);
    }

    private Set<UUID> handleOpenGameRepository(GameStartedEvent event) {
        var playerIdsForGame = gamePlayerRepository.fetchAllPlayerIdsForGame(event.getAggregateId());
        playerIdsForGame.forEach(x -> openGameForUserRepository
                .changeGameStage(x, event.getAggregateId(), GameStage.INPROGRESS));
        return playerIdsForGame;
    }

    private void handleGameListRepository(GameStartedEvent event) {
        gameListRepository.changeGameStage(event.getAggregateId(), GameStage.INPROGRESS);
    }

    private void handlePushNotifications(Set<UUID> playerIdsForGame) {
        playerIdsForGame.forEach(x -> pushNotificationPublisher
                .publish(new OpenGamesForPlayerUpdatedPushNotification(x)));
        pushNotificationPublisher.publish(GameListUpdatedPushNotification.INSTANCE);
    }

    private void handleChat(GameStartedEvent event) {
        var message = "Game started";
        chatService.saveAndPushSystemGameChatMessage(event.getAggregateId(), message);
    }
}
