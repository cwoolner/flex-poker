package com.flexpoker.game.command.commandreceivers;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.commands.GameCommand;
import com.flexpoker.game.command.commands.IncrementBlindsCommand;
import com.flexpoker.game.command.commands.JoinGameCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("gameCommandReceiver")
public class InMemoryAsyncGameCommandReceiver implements CommandReceiver<GameCommand> {

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
    public void receive(GameCommand command) {
        switch (command.getClass().getSimpleName()) {
        case "CreateGameCommand":
            createGameCommandHandler.handle((CreateGameCommand) command);
            break;
        case "JoinGameCommand":
            joinGameCommandHandler.handle((JoinGameCommand) command);
            break;
        case "AttemptToStartNewHandCommand":
            attemptToStartNewHandCommandHandler
                    .handle((AttemptToStartNewHandCommand) command);
            break;
        case "IncrementBlindsCommand":
            incrementBlindsCommandHandler.handle((IncrementBlindsCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getClass().getSimpleName());
        }
    }

}
