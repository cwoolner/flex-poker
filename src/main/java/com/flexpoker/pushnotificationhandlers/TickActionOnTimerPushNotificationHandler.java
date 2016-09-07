package com.flexpoker.pushnotificationhandlers;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification;
import com.flexpoker.util.MessagingConstants;

@Component
public class TickActionOnTimerPushNotificationHandler
        implements PushNotificationHandler<TickActionOnTimerPushNotification> {

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public TickActionOnTimerPushNotificationHandler(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    @Override
    public void handle(TickActionOnTimerPushNotification pushNotification) {
        messagingTemplate.convertAndSend(
                String.format(MessagingConstants.TICK_ACTION_ON_TIMER,
                pushNotification.getGameId(), pushNotification.getTableId()),
                pushNotification.getNumber());
    }

}
