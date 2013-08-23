package com.flexpoker.core.chat;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.model.chat.outgoing.GameChatMessage;

public class SendSimpleGameChatMessageCommandTest {

    @Mock private MessageSendingOperations<String> mockMessageSendingOperations;
    
    private SendSimpleGameChatMessageCommand command;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SendSimpleGameChatMessageCommand(mockMessageSendingOperations);
    }
    
    @Test
    public void testNonSystemUser() {
//        UUID gameId = UUID.randomUUID();
        Integer gameId = 5;
        command.execute(new GameChatMessage("this is a message from a user", "testuser", false, gameId));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/game/user/" + gameId, "testuser: this is a message from a user");
    }
    
    @Test
    public void testSystemUser() {
//        UUID gameId = UUID.randomUUID();
        Integer gameId = 5;
        command.execute(new GameChatMessage("this is a message from the system", null, true, gameId));
        verify(mockMessageSendingOperations).convertAndSend("/topic/chat/game/system/" + gameId, "System: this is a message from the system");
    }

}
