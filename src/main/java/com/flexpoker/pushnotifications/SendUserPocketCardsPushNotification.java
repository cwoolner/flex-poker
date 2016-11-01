package com.flexpoker.pushnotifications;

import java.util.UUID;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;
import com.flexpoker.model.card.PocketCards;

public class SendUserPocketCardsPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.SendUserPocketCards;

    private final UUID playerId;

    private final UUID handId;

    private final PocketCards pocketCards;

    public SendUserPocketCardsPushNotification(UUID playerId, UUID handId, PocketCards pocketCards) {
        super(TYPE);
        this.playerId = playerId;
        this.handId = handId;
        this.pocketCards = pocketCards;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getHandId() {
        return handId;
    }

    public PocketCards getPocketCards() {
        return pocketCards;
    }

}
