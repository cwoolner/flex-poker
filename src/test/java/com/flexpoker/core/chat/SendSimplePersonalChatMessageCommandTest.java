package com.flexpoker.core.chat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.model.chat.outgoing.PersonalChatMessage;
import com.flexpoker.repository.api.UserDataRepository;

public class SendSimplePersonalChatMessageCommandTest {

    @Mock private SimpMessageSendingOperations mockMessageSendingOperations;
    
    @Mock private UserDataRepository mockUserDataRepository;
    
    private SendSimplePersonalChatMessageCommand command;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SendSimplePersonalChatMessageCommand(
                mockMessageSendingOperations, mockUserDataRepository);
    }
    
    @Test
    public void testNonSystemUser() {
        UUID receiver1UUID = UUID.randomUUID();
        UUID receiver2UUID = UUID.randomUUID();
        String[] receiverUsernames = {"receiver1", "receiver2"};
        when(mockUserDataRepository.getPersonalChatId("receiver1")).thenReturn(receiver1UUID);
        when(mockUserDataRepository.getPersonalChatId("receiver2")).thenReturn(receiver2UUID);
        command.execute(new PersonalChatMessage("this is a message from a user", "testuser", false, Arrays.asList(receiverUsernames)));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/personal/user/" + receiver1UUID, "testuser: this is a message from a user");
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/personal/user/" + receiver2UUID, "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
        UUID receiver1UUID = UUID.randomUUID();
        UUID receiver2UUID = UUID.randomUUID();
        String[] receiverUsernames = {"receiver1", "receiver2"};
        when(mockUserDataRepository.getPersonalChatId("receiver1")).thenReturn(receiver1UUID);
        when(mockUserDataRepository.getPersonalChatId("receiver2")).thenReturn(receiver2UUID);
        command.execute(new PersonalChatMessage("this is a message from the system", null, true, Arrays.asList(receiverUsernames)));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/personal/system/" + receiver1UUID, "System: this is a message from the system");
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/personal/system/" + receiver2UUID, "System: this is a message from the system");
    }

}
