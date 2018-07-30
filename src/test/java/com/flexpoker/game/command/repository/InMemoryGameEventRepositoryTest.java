package com.flexpoker.game.command.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;

public class InMemoryGameEventRepositoryTest {

    @Test
    public void testFetchAll() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.save(new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10));
        var events = repository.fetchAll(gameId);
        assertEquals(1, events.size());
    }

    @Test
    public void testFetchGameCreatedEventSuccess() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.save(new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10));
        repository.save(new GameJoinedEvent(gameId, UUID.randomUUID()));
        var event = repository.fetchGameCreatedEvent(gameId);
        assertNotNull(event);
    }

    @Test
    public void testFetchGameCreatedEventFail() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.save(new GameJoinedEvent(gameId, UUID.randomUUID()));
        var event = repository.fetchGameCreatedEvent(gameId);
        assertNull(event);
    }

}
