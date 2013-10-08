package com.flexpoker.web.notifier;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.config.Notifier;
import com.flexpoker.event.SendUserPocketCardsEvent;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.PocketCardsViewModel;

@Notifier
public class SendUserPocketCardsNotifier implements ApplicationListener<SendUserPocketCardsEvent> {

    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public SendUserPocketCardsNotifier(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void onApplicationEvent(SendUserPocketCardsEvent event) {
        PocketCardsViewModel pocketCardsViewModel = new PocketCardsViewModel(
                event.getPocketCards().getCard1().getId(),
                event.getPocketCards().getCard2().getId(),
                event.getTableId());
        messagingTemplate.convertAndSendToUser(event.getUsername(),
                MessagingConstants.POCKET_CARDS, pocketCardsViewModel);
    }

}
