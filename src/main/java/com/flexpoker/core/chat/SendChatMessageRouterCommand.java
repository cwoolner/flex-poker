package com.flexpoker.core.chat;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendChatMessageCommand;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.chat.SendGlobalChatMessageCommand;
import com.flexpoker.core.api.chat.SendPersonalChatMessageCommand;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.model.chat.incoming.ChatMessage;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.model.chat.outgoing.GlobalChatMessage;
import com.flexpoker.model.chat.outgoing.PersonalChatMessage;
import com.flexpoker.model.chat.outgoing.TableChatMessage;

@Command
public class SendChatMessageRouterCommand implements SendChatMessageCommand {

    private final SendGlobalChatMessageCommand sendGlobalChatMessageCommand;

    private final SendGameChatMessageCommand sendGameChatMessageCommand;

    private final SendTableChatMessageCommand sendTableChatMessageCommand;

    private final SendPersonalChatMessageCommand sendPersonalChatMessageCommand;

    @Inject
    public SendChatMessageRouterCommand(
            SendGlobalChatMessageCommand sendGlobalChatMessageCommand,
            SendGameChatMessageCommand sendGameChatMessageCommand,
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SendPersonalChatMessageCommand sendPersonalChatMessageCommand) {
        this.sendGlobalChatMessageCommand = sendGlobalChatMessageCommand;
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.sendPersonalChatMessageCommand = sendPersonalChatMessageCommand;
    }

    @Override
    public void execute(ChatMessage chatMessage, Principal principal) {
        if (chatMessage.getReceiverUsernames() == null
                && chatMessage.getGameId() == null
                && chatMessage.getTableId() == null
                && chatMessage.getMessage() != null) {
            sendGlobalChatMessageCommand.execute(new GlobalChatMessage(chatMessage.getMessage(),
                    principal.getName(), false));
        } else if (chatMessage.getReceiverUsernames() == null
                && chatMessage.getGameId() != null
                && chatMessage.getTableId() == null
                && chatMessage.getMessage() != null) {
            GameChatMessage gameChatMessage = new GameChatMessage(chatMessage.getMessage(),
                    principal.getName(), false, UUID.fromString(chatMessage.getGameId()));
            sendGameChatMessageCommand.execute(gameChatMessage);
        } else if (chatMessage.getReceiverUsernames() == null
                && chatMessage.getGameId() != null
                && chatMessage.getTableId() != null
                && chatMessage.getMessage() != null) {
            sendTableChatMessageCommand.execute(new TableChatMessage(
                    chatMessage.getMessage(), principal.getName(), false,
                    Integer.parseInt(chatMessage.getGameId()),
                    Integer.parseInt(chatMessage.getTableId())));
        } else if (chatMessage.getReceiverUsernames() != null
                && chatMessage.getMessage() != null) {
            sendPersonalChatMessageCommand.execute(new PersonalChatMessage(chatMessage.getMessage(),
                    principal.getName(), false, chatMessage.getReceiverUsernames()));
        } else {
            throw new IllegalArgumentException("Message does not fit one of the prescribed types. chatMessage: " + chatMessage);
        }
    }

}
