package com.flexpoker.core.chat;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.model.chat.GlobalChatMessage;

public class SendSimpleGlobalChatMessageCommandTest {

    private MessageSendingOperations<String> mockMessageSendingOperations;
    
    private SendSimpleGlobalChatMessageCommand command;
    
    @Before
    public void setup() {
        mockMessageSendingOperations = mock(MessageSendingOperations.class);
        command = new SendSimpleGlobalChatMessageCommand(mockMessageSendingOperations);
    }
    
    @Test
    public void testNonSystemUser() {
        command.execute(new GlobalChatMessage("this is a message from a user", "testuser", false));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/global/user", "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
        command.execute(new GlobalChatMessage("this is a message from the system", null, true));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/global/system", "System: this is a message from the system");
    }

}
