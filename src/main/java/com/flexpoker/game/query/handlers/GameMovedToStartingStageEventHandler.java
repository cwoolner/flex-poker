package com.flexpoker.game.query.handlers;

import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.GamePlayerRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;

@Component
public class GameMovedToStartingStageEventHandler implements
        EventHandler<GameMovedToStartingStageEvent> {

    private final GameListRepository gameListRepository;

    private final GamePlayerRepository gamePlayerRepository;

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    private final SendGameChatMessageCommand sendGameChatMessageCommand;

    @Inject
    public GameMovedToStartingStageEventHandler(GameListRepository gameListRepository,
            GamePlayerRepository gamePlayerRepository,
            OpenGameForPlayerRepository openGameForUserRepository,
            PushNotificationPublisher pushNotificationPublisher,
            SendGameChatMessageCommand sendGameChatMessageCommand) {
        this.gameListRepository = gameListRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.openGameForUserRepository = openGameForUserRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
    }

    @Async
    @Override
    public void handle(GameMovedToStartingStageEvent event) {
        Set<UUID> playerIdsForGame = handleOpenGameRepository(event);
        handleGameListRepository(event);
        handlePushNotifications(playerIdsForGame);
        handleChat(event);
    }

    private Set<UUID> handleOpenGameRepository(GameMovedToStartingStageEvent event) {
        Set<UUID> playerIdsForGame = gamePlayerRepository.fetchAllPlayerIdsForGame(event
                .getAggregateId());
        playerIdsForGame.forEach(x -> openGameForUserRepository.changeGameStage(x,
                event.getAggregateId(), GameStage.STARTING));
        return playerIdsForGame;
    }

    private void handleGameListRepository(GameMovedToStartingStageEvent event) {
        gameListRepository.changeGameStage(event.getAggregateId(), GameStage.STARTING);
    }

    private void handlePushNotifications(Set<UUID> playerIdsForGame) {
        playerIdsForGame.forEach(x -> pushNotificationPublisher
                .publish(new OpenGamesForPlayerUpdatedPushNotification(x)));
        pushNotificationPublisher.publish(new GameListUpdatedPushNotification());
    }

    private void handleChat(GameMovedToStartingStageEvent event) {
        sendGameChatMessageCommand.execute(new GameChatMessage(
                "Game will be starting shortly", null, true, event.getAggregateId()));
    }
}
