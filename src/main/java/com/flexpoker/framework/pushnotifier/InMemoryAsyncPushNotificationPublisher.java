package com.flexpoker.framework.pushnotifier;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.pushnotifications.GameListUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification;
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification;
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification;

@Component
public class InMemoryAsyncPushNotificationPublisher implements PushNotificationPublisher {

    private final PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler;

    private final PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> openGamesForPlayerUpdatedPushNotificationHandler;

    private final PushNotificationHandler<OpenTableForUserPushNotification> openTableForUserPushNotificationHandler;

    private final PushNotificationHandler<SendUserPocketCardsPushNotification> sendUserPocketCardsPushNotificationHandler;

    private final PushNotificationHandler<TableUpdatedPushNotification> tableUpdatedPushNotificationHandler;

    private final PushNotificationHandler<TickActionOnTimerPushNotification> tickActionOnTimerPushNotificationHandler;

    @Inject
    public InMemoryAsyncPushNotificationPublisher(
            PushNotificationHandler<GameListUpdatedPushNotification> gameListUpdatedPushNotificationHandler,
            PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> openGamesForPlayerUpdatedPushNotificationHandler,
            PushNotificationHandler<OpenTableForUserPushNotification> openTableForUserPushNotificationHandler,
            PushNotificationHandler<SendUserPocketCardsPushNotification> sendUserPocketCardsPushNotificationHandler,
            PushNotificationHandler<TableUpdatedPushNotification> tableUpdatedPushNotificationHandler,
            PushNotificationHandler<TickActionOnTimerPushNotification> tickActionOnTimerPushNotificationHandler) {
        this.gameListUpdatedPushNotificationHandler = gameListUpdatedPushNotificationHandler;
        this.openGamesForPlayerUpdatedPushNotificationHandler = openGamesForPlayerUpdatedPushNotificationHandler;
        this.openTableForUserPushNotificationHandler = openTableForUserPushNotificationHandler;
        this.sendUserPocketCardsPushNotificationHandler = sendUserPocketCardsPushNotificationHandler;
        this.tableUpdatedPushNotificationHandler = tableUpdatedPushNotificationHandler;
        this.tickActionOnTimerPushNotificationHandler = tickActionOnTimerPushNotificationHandler;
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
            break;
        case OpenTableForUser:
            openTableForUserPushNotificationHandler
                    .handle((OpenTableForUserPushNotification) pushNotification);
            break;
        case SendUserPocketCards:
            sendUserPocketCardsPushNotificationHandler
                    .handle((SendUserPocketCardsPushNotification) pushNotification);
            break;
        case TableUpdated:
            tableUpdatedPushNotificationHandler
                    .handle((TableUpdatedPushNotification) pushNotification);
            break;
        case TickActionOnTimer:
            tickActionOnTimerPushNotificationHandler
                    .handle((TickActionOnTimerPushNotification) pushNotification);
            break;
        default:
            throw new IllegalArgumentException(
                    "Push Notification Type cannot be handled: "
                            + pushNotification.getType());
        }
    }

}
