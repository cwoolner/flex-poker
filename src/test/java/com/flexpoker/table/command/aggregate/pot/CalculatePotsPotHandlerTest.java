package com.flexpoker.table.command.aggregate.pot;

import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createBasicPotHandler;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class CalculatePotsPotHandlerTest {

    @Test
    public void testTwoPlayersSameBet() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);

        Map<UUID, Integer> chipsInFrontMap = new HashMap<>();
        chipsInFrontMap.put(player1, 10);
        chipsInFrontMap.put(player2, 10);

        Map<UUID, Integer> chipsInBackMap = new HashMap<>();
        chipsInBackMap.put(player1, 1490);
        chipsInBackMap.put(player2, 1490);

        Set<UUID> playersStillInHand = new HashSet<>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        List<TableEvent> potEvents = potHandler.calculatePots(1,
                chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents,
                PotCreatedEvent.class, PotAmountIncreasedEvent.class);

        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
        assertEquals(20, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
    }

    @Test
    public void testTwoPlayerDifferentBets() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);

        Map<UUID, Integer> chipsInFrontMap = new HashMap<>();
        chipsInFrontMap.put(player1, 10);
        chipsInFrontMap.put(player2, 20);

        Map<UUID, Integer> chipsInBackMap = new HashMap<>();
        chipsInBackMap.put(player1, 1490);
        chipsInBackMap.put(player2, 1480);

        Set<UUID> playersStillInHand = new HashSet<>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        List<TableEvent> potEvents = potHandler.calculatePots(1,
                chipsInFrontMap, chipsInBackMap, playersStillInHand);

        verifyNumberOfEventsAndEntireOrderByType(potEvents,
                PotCreatedEvent.class, PotAmountIncreasedEvent.class,
                PotAmountIncreasedEvent.class);

        assertEquals(20, ((PotAmountIncreasedEvent) potEvents.get(1)).getAmountIncreased());
        assertEquals(10, ((PotAmountIncreasedEvent) potEvents.get(2)).getAmountIncreased());
        assertTrue(((PotCreatedEvent) potEvents.get(0)).getPotId()
                .equals(((PotAmountIncreasedEvent) potEvents.get(1)).getPotId()));
    }

    @Test
    public void testTwoPlayerOneAllIn() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);

        Map<UUID, Integer> chipsInFrontMap = new HashMap<>();
        chipsInFrontMap.put(player1, 1000);
        chipsInFrontMap.put(player2, 20);

        Map<UUID, Integer> chipsInBackMap = new HashMap<>();
        chipsInBackMap.put(player1, 500);
        chipsInBackMap.put(player2, 0);

        Set<UUID> playersStillInHand = new HashSet<>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        List<TableEvent> potEvents = potHandler.calculatePots(1,
                chipsInFrontMap, chipsInBackMap, playersStillInHand);

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
    public void testTwoPlayerChipAndAChair() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);

        Map<UUID, Integer> chipsInFrontMap = new HashMap<>();
        chipsInFrontMap.put(player1, 2);
        chipsInFrontMap.put(player2, 1);

        Map<UUID, Integer> chipsInBackMap = new HashMap<>();
        chipsInBackMap.put(player1, 1499);
        chipsInBackMap.put(player2, 0);

        Set<UUID> playersStillInHand = new HashSet<>();
        playersStillInHand.add(player1);
        playersStillInHand.add(player2);

        List<TableEvent> potEvents = potHandler.calculatePots(1,
                chipsInFrontMap, chipsInBackMap, playersStillInHand);

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
