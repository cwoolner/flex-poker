package com.flexpoker.pushnotifications;

import java.util.UUID;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;
import com.flexpoker.util.MessagingConstants;

public class ChatSentPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.ChatSent;

    private final UUID id;

    private final UUID gameId;

    private final UUID tableId;

    private final String message;

    private final String senderUsername;

    private final boolean isSystemMessage;

    private final String destination;

    public ChatSentPushNotification(UUID gameId, UUID tableId, String message, String senderUsername,
            boolean isSystemMessage) {
        super(TYPE);
        this.id = UUID.randomUUID();
        this.gameId = gameId;
        this.tableId = tableId;
        this.message = message;
        this.senderUsername = senderUsername;
        this.isSystemMessage = isSystemMessage;

        if (gameId != null && tableId != null) {
            this.destination = String.format(MessagingConstants.CHAT_TABLE, gameId, tableId);
        } else if (gameId != null) {
            this.destination = String.format(MessagingConstants.CHAT_GAME, gameId);
        } else {
            this.destination = MessagingConstants.CHAT_GLOBAL;
        }
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

    public String getDestination() {
        return destination;
    }

}
