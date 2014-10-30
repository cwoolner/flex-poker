package com.flexpoker.framework.pushnotifier;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;

@Component
public class InMemoryAsyncPushNotificationPublisher implements PushNotificationPublisher {

    private final PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler;

    @Inject
    public InMemoryAsyncPushNotificationPublisher(
            PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler) {
        this.gameListUpdatedPushNotificationHandler = gameListUpdatedPushNotificationHandler;
    }

    @Override
    public void publish(PushNotification pushNotification) {
        switch (pushNotification.getType()) {
        case GameListUpdated:
            gameListUpdatedPushNotificationHandler
                    .handle((GameListUpdatedPushNotification) pushNotification);
            break;

        default:
            throw new IllegalArgumentException(
                    "Push Notification Type cannot be handled: "
                            + pushNotification.getType());
        }
    }

}
