package com.flexpoker.pushnotifications;

import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class OpenTableForUserPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.OpenTableForUser;

    private final UUID gameId;

    private final UUID tableId;

    private final UUID playerId;

    @Inject
    public OpenTableForUserPushNotification(UUID gameId, UUID tableId, UUID playerId) {
        super(TYPE);
        this.gameId = gameId;
        this.tableId = tableId;
        this.playerId = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
