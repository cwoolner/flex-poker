package com.flexpoker.game.query.handlers;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class GameCreatedEventHandler implements EventHandler<GameCreatedEvent> {

    private final LoginRepository loginRepository;

    private final GameListRepository gameListRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public GameCreatedEventHandler(LoginRepository loginRepository,
            GameListRepository gameListRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.gameListRepository = gameListRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Async
    @Override
    public void handle(GameCreatedEvent event) {
        handleGameListRepository(event);
        handlePushNotifications();
    }

    private void handleGameListRepository(GameCreatedEvent event) {
        var createdByUsername = loginRepository.fetchUsernameByAggregateId(event.getCreatedByPlayerId());
        var gameInListDTO = new GameInListDTO(event.getAggregateId(),
                event.getGameName(), GameStage.REGISTERING.toString(), 0,
                event.getNumberOfPlayers(), event.getNumberOfPlayersPerTable(),
                event.getNumberOfMinutesBetweenBlindLevels(),
                event.getNumberOfSecondsForActionOnTimer(),
                createdByUsername, event.getTime().toString());

        gameListRepository.saveNew(gameInListDTO);
    }

    private void handlePushNotifications() {
        pushNotificationPublisher.publish(GameListUpdatedPushNotification.INSTANCE);
    }

}
