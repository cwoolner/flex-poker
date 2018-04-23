package com.flexpoker.pushnotifications;

import java.util.UUID;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class TableUpdatedPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.TableUpdated;

    private final UUID gameId;

    private final UUID tableId;

    public TableUpdatedPushNotification(UUID gameId, UUID tableId) {
        super(TYPE);
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
