package com.flexpoker.table.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.table.command.events.PlayerBustedTableEvent;

@Component
public class PlayerBustedTableEventHandler implements EventHandler<PlayerBustedTableEvent> {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;

    private final LoginRepository loginRepository;

    @Inject
    public PlayerBustedTableEventHandler(SendGameChatMessageCommand sendGameChatMessageCommand,
            LoginRepository loginRepository) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(PlayerBustedTableEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedTableEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " is out";
        sendGameChatMessageCommand.execute(
                new GameChatMessage(message, null, true, event.getAggregateId()));
    }

}
