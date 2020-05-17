package com.flexpoker.pushnotificationhandlers;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.pushnotifications.ChatSentPushNotification;

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
        messagingTemplate.convertAndSend(pushNotification.getDestination(), pushNotification);
    }

}
