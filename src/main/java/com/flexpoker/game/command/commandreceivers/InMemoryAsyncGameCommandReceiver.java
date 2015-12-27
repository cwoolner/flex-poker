package com.flexpoker.game.command.commandreceivers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.commands.IncrementBlindsCommand;
import com.flexpoker.game.command.commands.JoinGameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

@Component("gameCommandReceiver")
public class InMemoryAsyncGameCommandReceiver implements
        CommandReceiver<GameCommandType> {

    private final CommandHandler<CreateGameCommand> createGameCommandHandler;

    private final CommandHandler<JoinGameCommand> joinGameCommandHandler;

    private final CommandHandler<AttemptToStartNewHandCommand> attemptToStartNewHandCommandHandler;

    private final CommandHandler<IncrementBlindsCommand> incrementBlindsCommandHandler;

    @Inject
    public InMemoryAsyncGameCommandReceiver(
            CommandHandler<CreateGameCommand> createGameCommandHandler,
            CommandHandler<JoinGameCommand> joinGameCommandHandler,
            CommandHandler<AttemptToStartNewHandCommand> attemptToStartNewHandCommandHandler,
            CommandHandler<IncrementBlindsCommand> incrementBlindsCommandHandler) {
        this.createGameCommandHandler = createGameCommandHandler;
        this.joinGameCommandHandler = joinGameCommandHandler;
        this.attemptToStartNewHandCommandHandler = attemptToStartNewHandCommandHandler;
        this.incrementBlindsCommandHandler = incrementBlindsCommandHandler;
    }

    @Async
    @Override
    public void receive(Command<GameCommandType> command) {
        switch (command.getType()) {
        case CreateGame:
            createGameCommandHandler.handle((CreateGameCommand) command);
            break;
        case JoinGame:
            joinGameCommandHandler.handle((JoinGameCommand) command);
            break;
        case AttemptToStartNewHand:
            attemptToStartNewHandCommandHandler
                    .handle((AttemptToStartNewHandCommand) command);
            break;
        case IncrementBlinds:
            incrementBlindsCommandHandler.handle((IncrementBlindsCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
