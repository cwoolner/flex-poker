package com.flexpoker.game.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

@Component
public class PlayerBustedGameEventHandler implements EventHandler<PlayerBustedGameEvent> {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;

    private final LoginRepository loginRepository;

    @Inject
    public PlayerBustedGameEventHandler(SendGameChatMessageCommand sendGameChatMessageCommand,
            LoginRepository loginRepository) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(PlayerBustedGameEvent event) {
        handleChat(event);
    }

    private void handleChat(PlayerBustedGameEvent event) {
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());
        String message = username + " is out";
        sendGameChatMessageCommand.execute(
                new GameChatMessage(message, null, true, event.getAggregateId()));
    }

}
