package com.flexpoker.pushnotificationhandlers;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.pushnotifier.PushNotificationHandler;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.outgoing.OpenTableForUserDTO;

@Component
public class OpenTableForUserPushNotificationHandler implements
        PushNotificationHandler<OpenTableForUserPushNotification> {

    private final LoginRepository loginRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public OpenTableForUserPushNotificationHandler(LoginRepository loginRepository,
            SimpMessageSendingOperations messagingTemplate) {
        this.loginRepository = loginRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Async
    @Override
    public void handle(OpenTableForUserPushNotification pushNotification) {
        String username = loginRepository.fetchUsernameByAggregateId(pushNotification
                .getPlayerId());
        OpenTableForUserDTO dto = new OpenTableForUserDTO(pushNotification.getGameId(),
                pushNotification.getTableId());
        messagingTemplate.convertAndSendToUser(username,
                MessagingConstants.OPEN_TABLE_FOR_USER, dto);
    }

}
