package com.flexpoker.web.commandsenders;

import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.game.command.commands.GameCommand;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class InMemoryAsyncGameCommandSender implements CommandSender<GameCommand> {

    private final CommandReceiver<GameCommand> gameCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncGameCommandSender(CommandReceiver<GameCommand> gameCommandReceiver) {
        this.gameCommandReceiver = gameCommandReceiver;
    }

    @Override
    public void send(GameCommand command) {
        gameCommandReceiver.receive(command);
    }

}
