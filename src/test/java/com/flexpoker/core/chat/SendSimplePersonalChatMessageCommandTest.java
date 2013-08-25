package com.flexpoker.core.chat;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.model.chat.outgoing.PersonalChatMessage;

public class SendSimplePersonalChatMessageCommandTest {

    @Mock private SimpMessageSendingOperations mockMessageSendingOperations;
    
    private SendSimplePersonalChatMessageCommand command;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SendSimplePersonalChatMessageCommand(mockMessageSendingOperations);
    }
    
    @Test
    public void testNonSystemUser() {
        String[] receiverUsernames = {"receiver1", "receiver2"};
        command.execute(new PersonalChatMessage("this is a message from a user", "testuser", false, Arrays.asList(receiverUsernames)));
        verify(mockMessageSendingOperations).convertAndSendToUser("receiver1", "/topic/chat/personal/user", "testuser: this is a message from a user");
        verify(mockMessageSendingOperations).convertAndSendToUser("receiver2", "/topic/chat/personal/user", "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
        String[] receiverUsernames = {"receiver1", "receiver2"};
        command.execute(new PersonalChatMessage("this is a message from the system", null, true, Arrays.asList(receiverUsernames)));
        verify(mockMessageSendingOperations).convertAndSendToUser("receiver1", "/topic/chat/personal/system", "System: this is a message from the system");
        verify(mockMessageSendingOperations).convertAndSendToUser("receiver2", "/topic/chat/personal/system", "System: this is a message from the system");
    }

}
