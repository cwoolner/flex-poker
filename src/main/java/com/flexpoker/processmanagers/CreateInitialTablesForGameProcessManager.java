package com.flexpoker.processmanagers;

import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;

@Component
public class CreateInitialTablesForGameProcessManager implements
        ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> {

    // private final CommandPublisher<TableCommandType> tableCommandPublisher;

    // @Inject
    public CreateInitialTablesForGameProcessManager() {
    }

    @Async
    @Override
    public void handle(GameTablesCreatedAndPlayersAssociatedEvent event) {
        event.getTableIdToPlayerIdsMap().keySet().forEach(x -> new Consumer<UUID>() {
            @Override
            public void accept(UUID tableId) {
                // TODO: create a CreateTableEvent that takes an id and an
                // initial set of players

                // Set<UUID> playerIds =
                // event.getTableIdToPlayerIdsMap().get(tableId);
                // tableCommandPublisher.publish()
            }
        });

    }
}
