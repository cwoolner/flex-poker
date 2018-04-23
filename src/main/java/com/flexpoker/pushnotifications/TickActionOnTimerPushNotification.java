package com.flexpoker.pushnotifications;

import java.util.UUID;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class TickActionOnTimerPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.TickActionOnTimer;

    private final UUID gameId;

    private final UUID tableId;

    private final int number;

    public TickActionOnTimerPushNotification(UUID gameId, UUID tableId, int number) {
        super(TYPE);
        this.gameId = gameId;
        this.tableId = tableId;
        this.number = number;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public int getNumber() {
        return number;
    }

}
