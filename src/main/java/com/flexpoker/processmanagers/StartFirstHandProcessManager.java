package com.flexpoker.processmanagers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;

@Component
public class StartFirstHandProcessManager implements ProcessManager<GameStartedEvent> {

    // private final CommandPublisher<TableCommandType> tableCommandPublisher;

    // @Inject
    public StartFirstHandProcessManager() {
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        // setSeatStatusForNewGameCommand.execute(game, table);
        // startNewHandCommand.execute(game, table);
    }
}
