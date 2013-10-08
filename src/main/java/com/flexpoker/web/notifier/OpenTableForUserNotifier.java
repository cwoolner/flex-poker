package com.flexpoker.web.notifier;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.config.Notifier;
import com.flexpoker.event.OpenTableForUserEvent;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.OpenTableForUserViewModel;

@Notifier
public class OpenTableForUserNotifier implements ApplicationListener<OpenTableForUserEvent> {

    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public OpenTableForUserNotifier(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void onApplicationEvent(OpenTableForUserEvent event) {
        OpenTableForUserViewModel viewModel = new OpenTableForUserViewModel(
                event.getGameId(), event.getTableId());
        messagingTemplate.convertAndSendToUser(event.getUsername(),
                MessagingConstants.OPEN_TABLE_FOR_USER, viewModel);
    }

}
