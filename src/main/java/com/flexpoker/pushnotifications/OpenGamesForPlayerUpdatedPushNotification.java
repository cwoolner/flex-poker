package com.flexpoker.pushnotifications;

import java.util.UUID;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class OpenGamesForPlayerUpdatedPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.OpenGamesForPlayerUpdated;

    private final UUID playerId;

    public OpenGamesForPlayerUpdatedPushNotification(UUID playerId) {
        super(TYPE);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
