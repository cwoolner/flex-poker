package com.flexpoker.web.notifier;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.config.Notifier;
import com.flexpoker.event.TableUpdatedEvent;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.web.model.table.TableViewModel;
import com.flexpoker.web.translator.table.TableTranslator;

@Notifier
public class TableUpdatedNotifier implements ApplicationListener<TableUpdatedEvent> {

    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public TableUpdatedNotifier(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void onApplicationEvent(TableUpdatedEvent event) {
        TableViewModel tableViewModel = new TableTranslator().translate(event.getTable());
        messagingTemplate.convertAndSend(String.format(MessagingConstants.TABLE_STATUS,
                event.getGameId(), event.getTable().getId()), tableViewModel);
    }

}
