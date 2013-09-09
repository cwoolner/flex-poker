package com.flexpoker.core.chat;

import static org.mockito.Mockito.*;

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
        int gameId = 1;
        int tableId = 2;
        command.execute(new TableChatMessage("this is a message from a user",
                "testuser", false, gameId, tableId));
        verify(mockMessageSendingOperations).convertAndSend(
                "/topic/chat/game/" + gameId + "/table/" + tableId + "/user",
                "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
        int gameId = 1;
        int tableId = 2;
        command.execute(new TableChatMessage("this is a message from the system",
                null, true, gameId, tableId));
        verify(mockMessageSendingOperations).convertAndSend(
                "/topic/chat/game/" + gameId + "/table/" + tableId + "/system",
                "System: this is a message from the system");
    }

}
