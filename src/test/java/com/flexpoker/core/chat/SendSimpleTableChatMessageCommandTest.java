package com.flexpoker.core.chat;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.model.chat.outgoing.TableChatMessage;

public class SendSimpleTableChatMessageCommandTest {

    @Mock private SimpMessageSendingOperations mockMessageSendingOperations;
    
    private SendSimpleTableChatMessageCommand command;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SendSimpleTableChatMessageCommand(mockMessageSendingOperations);
    }
    
    @Test
    public void testNonSystemUser() {
        UUID tableId = UUID.randomUUID();
        command.execute(new TableChatMessage("this is a message from a user", "testuser", false, tableId));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/table/user/" + tableId, "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
        UUID tableId = UUID.randomUUID();
        command.execute(new TableChatMessage("this is a message from the system", null, true, tableId));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/table/system/" + tableId, "System: this is a message from the system");
    }

}
