package com.flexpoker.pushnotifications;

import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class TableUpdatedPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.TableUpdated;

    private final UUID gameId;

    private final UUID tableId;

    @Inject
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
