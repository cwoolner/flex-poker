package com.flexpoker.table.command.aggregate.pot;

import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createBasicPotHandler;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;

public class CalculatePotsPotHandlerTest {

    @Test
    void testTwoPlayersSameBet() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);

        var chipsInFrontMap = new HashMap<UUID, Integer>();
        chipsInFrontMap.put(player1, 10);
        chipsInFrontMap.put(player2, 10);

        var chipsInBackMap = new HashMap<UUID, Integer>();
        chipsInBackMap.put(player1, 1490);
        chipsInBackMap.put(player2, 1490);

        var playersStillInHand = new HashSet<UUID>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        var potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents, PotCreatedEvent.class, PotAmountIncreasedEvent.class);

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
        assertEquals(20, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
    }

    @Test
    void testTwoPlayerDifferentBets() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);

        var chipsInFrontMap = new HashMap<UUID, Integer>();
        chipsInFrontMap.put(player1, 10);
        chipsInFrontMap.put(player2, 20);

        var chipsInBackMap = new HashMap<UUID, Integer>();
        chipsInBackMap.put(player1, 1490);
        chipsInBackMap.put(player2, 1480);

        var playersStillInHand = new HashSet<UUID>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        var potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents, PotCreatedEvent.class, PotAmountIncreasedEvent.class,
                PotAmountIncreasedEvent.class);

        assertEquals(20, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
        assertEquals(10, ((PotAmountIncreasedEvent) potEvents.get(2)).getAmountIncreased());
        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
    }

    @Test
    void testTwoPlayerOneAllIn() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);

        var chipsInFrontMap = new HashMap<UUID, Integer>();
        chipsInFrontMap.put(player1, 1000);
        chipsInFrontMap.put(player2, 20);

        var chipsInBackMap = new HashMap<UUID, Integer>();
        chipsInBackMap.put(player1, 500);
        chipsInBackMap.put(player2, 0);

        var playersStillInHand = new HashSet<UUID>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        var potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents,
                PotCreatedEvent.class,
                PotAmountIncreasedEvent.class,
                PotClosedEvent.class,
                PotCreatedEvent.class,
                PotAmountIncreasedEvent.class);

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPlayersInvolved().contains(player1));
        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPlayersInvolved().contains(player2));
        assertEquals(40, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
        
        assertTrue(((PotCreatedEvent) potEvents.get(3)).getPlayersInvolved().contains(player1));
        assertFalse(((PotCreatedEvent) potEvents.get(3)).getPlayersInvolved().contains(player2));
        assertEquals(980, ((PotAmountIncreasedEvent) potEvents.get(4)).getAmountIncreased());

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
    }

    @Test
    void testTwoPlayerChipAndAChair() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);

        var chipsInFrontMap = new HashMap<UUID, Integer>();
        chipsInFrontMap.put(player1, 2);
        chipsInFrontMap.put(player2, 1);

        var chipsInBackMap = new HashMap<UUID, Integer>();
        chipsInBackMap.put(player1, 1499);
        chipsInBackMap.put(player2, 0);

        var playersStillInHand = new HashSet<UUID>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        var potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents,
                PotCreatedEvent.class,
                PotAmountIncreasedEvent.class,
                PotClosedEvent.class,
                PotCreatedEvent.class,
                PotAmountIncreasedEvent.class);

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPlayersInvolved().contains(player1));
        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPlayersInvolved().contains(player2));
        assertEquals(2, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
        
        assertTrue(((PotCreatedEvent) potEvents.get(3)).getPlayersInvolved().contains(player1));
        assertFalse(((PotCreatedEvent) potEvents.get(3)).getPlayersInvolved().contains(player2));
        assertEquals(1, ((PotAmountIncreasedEvent) potEvents.get(4)).getAmountIncreased());

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
    }

}
