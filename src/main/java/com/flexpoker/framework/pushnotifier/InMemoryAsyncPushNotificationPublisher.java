package com.flexpoker.framework.pushnotifier;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;

@Component
public class InMemoryAsyncPushNotificationPublisher implements PushNotificationPublisher {

    private final PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler;

    private final PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> openGamesForPlayerUpdatedPushNotificationHandler;

    @Inject
    public InMemoryAsyncPushNotificationPublisher(
            PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler,
            PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> openGamesForPlayerUpdatedPushNotificationHandler) {
        this.gameListUpdatedPushNotificationHandler = gameListUpdatedPushNotificationHandler;
        this.openGamesForPlayerUpdatedPushNotificationHandler = openGamesForPlayerUpdatedPushNotificationHandler;
    }

    @Async
    @Override
    public void publish(PushNotification pushNotification) {
        switch (pushNotification.getType()) {
        case GameListUpdated:
            gameListUpdatedPushNotificationHandler
                    .handle((GameListUpdatedPushNotification) pushNotification);
            break;
        case OpenGamesForPlayerUpdated:
            openGamesForPlayerUpdatedPushNotificationHandler
                    .handle((OpenGamesForPlayerUpdatedPushNotification) pushNotification);
        default:
            throw new IllegalArgumentException(
                    "Push Notification Type cannot be handled: "
                            + pushNotification.getType());
        }
    }

}
