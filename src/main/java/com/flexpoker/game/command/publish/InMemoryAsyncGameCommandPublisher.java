package com.flexpoker.game.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.commands.JoinGameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

@Component
public class InMemoryAsyncGameCommandPublisher implements
        CommandPublisher<GameCommandType> {

    private final CommandHandler<CreateGameCommand> createGameCommandHandler;

    private final CommandHandler<JoinGameCommand> joinGameCommandHandler;

    @Inject
    public InMemoryAsyncGameCommandPublisher(
            CommandHandler<CreateGameCommand> createGameCommandHandler,
            CommandHandler<JoinGameCommand> joinGameCommandHandler) {
        this.createGameCommandHandler = createGameCommandHandler;
        this.joinGameCommandHandler = joinGameCommandHandler;
    }

    @Override
    public void publish(Command<GameCommandType> command) {
        switch (command.getType()) {
        case CreateGame:
            createGameCommandHandler.handle((CreateGameCommand) command);
            break;
        case JoinGame:
            joinGameCommandHandler.handle((JoinGameCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
