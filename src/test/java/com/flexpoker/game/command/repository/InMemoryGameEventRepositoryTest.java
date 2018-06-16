package com.flexpoker.game.command.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;

public class InMemoryGameEventRepositoryTest {

    @Test
    public void testFetchAll() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(0, Collections.singletonList(
                new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10)));
        var events = repository.fetchAll(gameId);
        assertEquals(1, events.size());
    }

    @Test
    public void testFetchGameCreatedEventSuccess() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(0, Collections.singletonList(
                new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10)));
        repository.setEventVersionsAndSave(1, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
        var event = repository.fetchGameCreatedEvent(gameId);
        assertNotNull(event);
    }

    @Test
    public void testFetchGameCreatedEventFail() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(0, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
        var event = repository.fetchGameCreatedEvent(gameId);
        assertNull(event);
    }

    @Test(expected = FlexPokerException.class)
    public void testSetEventVersionsAndSaveBadBasedOnVersion1() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(1, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
    }

    @Test(expected = FlexPokerException.class)
    public void testSetEventVersionsAndSaveTwoJoinsSameTime() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(0, Collections.singletonList(
                new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10)));
        repository.setEventVersionsAndSave(1, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
        repository.setEventVersionsAndSave(1, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
    }

    @Test
    public void testSetEventVersionsAndSaveTwoJoinsSeparateTime() {
        var repository = new InMemoryGameEventRepository();
        var gameId = UUID.randomUUID();
        repository.setEventVersionsAndSave(0, Collections.singletonList(
                new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 10)));
        repository.setEventVersionsAndSave(1, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));
        repository.setEventVersionsAndSave(2, Collections.singletonList(
                new GameJoinedEvent(gameId, UUID.randomUUID())));

        assertEquals(3, repository.fetchAll(gameId).size());
        assertEquals(1, repository.fetchAll(gameId).get(0).getVersion());
        assertEquals(GameCreatedEvent.class, repository.fetchAll(gameId).get(0).getClass());
        assertEquals(2, repository.fetchAll(gameId).get(1).getVersion());
        assertEquals(GameJoinedEvent.class, repository.fetchAll(gameId).get(1).getClass());
        assertEquals(3, repository.fetchAll(gameId).get(2).getVersion());
        assertEquals(GameJoinedEvent.class, repository.fetchAll(gameId).get(2).getClass());
    }

}
