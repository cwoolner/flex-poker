package com.flexpoker.pushnotifications;

import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;
import com.flexpoker.model.card.PocketCards;

public class SendUserPocketCardsPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.SendUserPocketCards;

    private final PocketCards pocketCards;

    private final UUID tableId;

    private final UUID playerId;

    @Inject
    public SendUserPocketCardsPushNotification(UUID playerId, PocketCards pocketCards,
            UUID tableId) {
        super(TYPE);
        this.playerId = playerId;
        this.pocketCards = pocketCards;
        this.tableId = tableId;
    }

    public PocketCards getPocketCards() {
        return pocketCards;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
