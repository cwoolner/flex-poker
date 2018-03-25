package com.flexpoker.game.command.repository;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class InMemoryGameEventRepositoryTest {

    @Test
    public void testFetchAll() {
        InMemoryGameEventRepository repository = new InMemoryGameEventRepository();
        UUID gameId = UUID.randomUUID();
        repository.save(new GameCreatedEvent(gameId , 1, "test", 2, 2, UUID.randomUUID(), 10, 10));
        List<GameEvent> events = repository.fetchAll(gameId);
        assertEquals(1, events.size());
    }

    @Test
    public void testFetchGameCreatedEventSuccess() {
        InMemoryGameEventRepository repository = new InMemoryGameEventRepository();
        UUID gameId = UUID.randomUUID();
        repository.save(new GameCreatedEvent(gameId , 1, "test", 2, 2, UUID.randomUUID(), 10, 10));
        repository.save(new GameJoinedEvent(gameId, 2, UUID.randomUUID()));
        GameCreatedEvent event = repository.fetchGameCreatedEvent(gameId);
        assertNotNull(event);
    }

    @Test
    public void testFetchGameCreatedEventFail() {
        InMemoryGameEventRepository repository = new InMemoryGameEventRepository();
        UUID gameId = UUID.randomUUID();
        repository.save(new GameJoinedEvent(gameId, 1, UUID.randomUUID()));
        GameCreatedEvent event = repository.fetchGameCreatedEvent(gameId);
        assertNull(event);
    }

}
