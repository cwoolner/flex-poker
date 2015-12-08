package com.flexpoker.web.commandsenders;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.game.command.framework.GameCommandType;

@Component
public class InMemoryAsyncGameCommandSender
        implements CommandSender<GameCommandType> {

    private final CommandReceiver<GameCommandType> gameCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncGameCommandSender(
            @Qualifier("gameCommandReceiver")
            CommandReceiver<GameCommandType> gameCommandReceiver) {
        this.gameCommandReceiver = gameCommandReceiver;
    }

    @Override
    public void send(Command<GameCommandType> command) {
        gameCommandReceiver.receive(command);
    }

}
