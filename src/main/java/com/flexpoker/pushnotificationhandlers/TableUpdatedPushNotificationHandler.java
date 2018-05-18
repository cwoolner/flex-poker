package com.flexpoker.pushnotificationhandlers;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.util.MessagingConstants;

@Component
public class TableUpdatedPushNotificationHandler implements PushNotificationHandler<TableUpdatedPushNotification> {

    private final SimpMessageSendingOperations messagingTemplate;

    private final TableRepository tableRepository;

    @Inject
    public TableUpdatedPushNotificationHandler(SimpMessageSendingOperations messagingTemplate,
            TableRepository tableRepository) {
        this.messagingTemplate = messagingTemplate;
        this.tableRepository = tableRepository;
    }

    @Async
    @Override
    public void handle(TableUpdatedPushNotification pushNotification) {
        var tableDTO = tableRepository.fetchById(pushNotification.getTableId());
        messagingTemplate.convertAndSend(String.format(MessagingConstants.TABLE_STATUS, pushNotification.getGameId(),
                pushNotification.getTableId()), tableDTO);
    }

}
