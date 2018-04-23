package com.flexpoker.pushnotificationhandlers;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.util.MessagingConstants;

@Component
public class ChatSentPushNotificationHandler implements PushNotificationHandler<ChatSentPushNotification> {

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public ChatSentPushNotificationHandler(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    @Override
    public void handle(ChatSentPushNotification pushNotification) {
        if (pushNotification.getGameId() != null && pushNotification.getTableId() != null) {
            String topic = String.format(MessagingConstants.CHAT_TABLE, pushNotification.getGameId(),
                    pushNotification.getTableId());
            messagingTemplate.convertAndSend(topic, pushNotification);
        } else if (pushNotification.getGameId() != null) {
            String topic = String.format(MessagingConstants.CHAT_GAME, pushNotification.getGameId());
            messagingTemplate.convertAndSend(topic, pushNotification);
        } else {
            messagingTemplate.convertAndSend(MessagingConstants.CHAT_GLOBAL, pushNotification);
        }

    }

}
