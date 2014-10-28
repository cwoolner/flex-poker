package com.flexpoker.game.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;

@Component
public class DefaultGameFactory implements GameFactory {

    @Override
    public Game createNew() {
        UUID aggregateId = UUID.randomUUID();
        return createWithGivenId(aggregateId);
    }

    @Override
    public Game createFrom(List<GameEvent> events) {
        Game game = createWithGivenId(events.get(0).getAggregateId());
        game.applyAllEvents(events);
        return game;
    }

    private Game createWithGivenId(UUID aggregateId) {
        return new Game(aggregateId);
    }

}
