package com.flexpoker.core.chat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.chat.SendGlobalChatMessageCommand;
import com.flexpoker.core.api.chat.SendPersonalChatMessageCommand;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.model.chat.incoming.ChatMessage;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.model.chat.outgoing.GlobalChatMessage;
import com.flexpoker.model.chat.outgoing.PersonalChatMessage;
import com.flexpoker.model.chat.outgoing.TableChatMessage;

public class SendChatMessageRouterCommandTest {

    private SendChatMessageRouterCommand command;

    @Mock private Principal mockPrincipal;

    @Mock private SendGlobalChatMessageCommand mockSendGlobalChatMessageCommand;

    @Mock private SendGameChatMessageCommand mockSendGameChatMessageCommand;

    @Mock private SendTableChatMessageCommand mockSendTableChatMessageCommand;

    @Mock private SendPersonalChatMessageCommand mockSendPersonalChatMessageCommand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockPrincipal.getName()).thenReturn("testuser");
        command = new SendChatMessageRouterCommand(
                mockSendGlobalChatMessageCommand,
                mockSendGameChatMessageCommand,
                mockSendTableChatMessageCommand,
                mockSendPersonalChatMessageCommand);
    }
    
    @Test
    public void testGlobalMessage() {
        ChatMessage globalMessage = new ChatMessage("global message", null, null, null);

        command.execute(globalMessage, mockPrincipal);

        ArgumentCaptor<GlobalChatMessage> argument = ArgumentCaptor.forClass(GlobalChatMessage.class);
        verify(mockSendGlobalChatMessageCommand).execute(argument.capture());
        assertEquals("global message", argument.getValue().getMessage());
        assertEquals("testuser", argument.getValue().getSenderUsername());
        assertEquals(false, argument.getValue().isSystemMessage());
    }

    @Test
    public void testGameMessage() {
        UUID gameId = UUID.randomUUID();
        ChatMessage gameMessage = new ChatMessage("game message", null, gameId.toString(), null);

        command.execute(gameMessage, mockPrincipal);

        ArgumentCaptor<GameChatMessage> argument = ArgumentCaptor.forClass(GameChatMessage.class);
        verify(mockSendGameChatMessageCommand).execute(argument.capture());
        assertEquals("game message", argument.getValue().getMessage());
        assertEquals("testuser", argument.getValue().getSenderUsername());
        assertEquals(false, argument.getValue().isSystemMessage());
        assertEquals(gameId, argument.getValue().getGameId());
    }

    @Test
    public void testTableMessage() {
        UUID gameId = UUID.randomUUID();
        UUID tableId = UUID.randomUUID();
        ChatMessage tableMessage = new ChatMessage("table message", null,
                gameId.toString(), tableId.toString());

        command.execute(tableMessage, mockPrincipal);

        ArgumentCaptor<TableChatMessage> argument = ArgumentCaptor.forClass(TableChatMessage.class);
        verify(mockSendTableChatMessageCommand).execute(argument.capture());
        assertEquals("table message", argument.getValue().getMessage());
        assertEquals("testuser", argument.getValue().getSenderUsername());
        assertEquals(false, argument.getValue().isSystemMessage());
        assertEquals(tableId, argument.getValue().getTableId());
    }

    @Test
    public void testPersonalMessage() {
        String[] receiverUsernames = {"receiver1", "receiver2"};
        ChatMessage personalMessage = new ChatMessage("personal message", Arrays.asList(receiverUsernames), null, null);

        command.execute(personalMessage, mockPrincipal);

        ArgumentCaptor<PersonalChatMessage> argument = ArgumentCaptor.forClass(PersonalChatMessage.class);
        verify(mockSendPersonalChatMessageCommand).execute(argument.capture());
        assertEquals("personal message", argument.getValue().getMessage());
        assertEquals("testuser", argument.getValue().getSenderUsername());
        assertEquals(false, argument.getValue().isSystemMessage());
        assertEquals("receiver1", argument.getValue().getReceiverUsernames().get(0));
        assertEquals("receiver2", argument.getValue().getReceiverUsernames().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid() {
        ChatMessage chatMessage = new ChatMessage(null, null, null, null);
        command.execute(chatMessage, mockPrincipal);
    }

}
