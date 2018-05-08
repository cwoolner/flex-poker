package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.fetchIdForBigBlind;
import static com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.fetchIdForButton;
import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.HandDealerState;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.PlayerForceFoldedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;

public class TwoPlayerButtonFoldsDueToTimeoutTest {

    @Test
    public void test() {
        var tableId = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID());

        // use the info in action on event to simulate the expire
        var actionOnChangedEvent = (ActionOnChangedEvent) table.fetchNewEvents().get(4);
        table.expireActionOn(actionOnChangedEvent.getHandId(), actionOnChangedEvent.getPlayerId());

        var newEvents = table.fetchNewEvents();
        var tableCreatedEvent = (TableCreatedEvent) newEvents.get(0);
        var handDealtEvent = (HandDealtEvent) newEvents.get(2);
        var potCreatedEvent = (PotCreatedEvent) newEvents.get(3);
        var smallBlindPotAmountIncreasedEvent = (PotAmountIncreasedEvent) newEvents.get(6);
        var bigBlindPotAmountIncreasedEvent = (PotAmountIncreasedEvent) newEvents.get(7);
        var roundCompletedEvent = (RoundCompletedEvent) newEvents.get(8);

        var buttonPlayerId = fetchIdForButton(tableCreatedEvent, handDealtEvent);
        var bigBlindPlayerId = fetchIdForBigBlind(tableCreatedEvent, handDealtEvent);

        assertEquals(10, handDealtEvent.getCallAmountsMap().get(buttonPlayerId).intValue());
        assertEquals(0, handDealtEvent.getCallAmountsMap().get(bigBlindPlayerId).intValue());
        assertEquals(1490, handDealtEvent.getChipsInBack().get(buttonPlayerId).intValue());
        assertEquals(1480, handDealtEvent.getChipsInBack().get(bigBlindPlayerId).intValue());
        assertEquals(10, handDealtEvent.getChipsInFrontMap().get(buttonPlayerId).intValue());
        assertEquals(20, handDealtEvent.getChipsInFrontMap().get(bigBlindPlayerId).intValue());
        assertEquals(HandDealerState.POCKET_CARDS_DEALT, handDealtEvent.getHandDealerState());

        assertEquals(2, potCreatedEvent.getPlayersInvolved().size());
        assertTrue(potCreatedEvent.getPlayersInvolved().contains(buttonPlayerId));
        assertTrue(potCreatedEvent.getPlayersInvolved().contains(bigBlindPlayerId));

        assertEquals(20, smallBlindPotAmountIncreasedEvent.getAmountIncreased());
        assertEquals(10, bigBlindPotAmountIncreasedEvent.getAmountIncreased());

        assertEquals(HandDealerState.COMPLETE, roundCompletedEvent.getNextHandDealerState());

        var winnersDeterminedEvent = ((WinnersDeterminedEvent) newEvents.get(9));
        var playersToChipsWonMap = winnersDeterminedEvent.getPlayersToChipsWonMap();
        var playersToShowCards = winnersDeterminedEvent.getPlayersToShowCards();

        assertNull(playersToChipsWonMap.get(buttonPlayerId));
        assertEquals(30, playersToChipsWonMap.get(bigBlindPlayerId).intValue());
        assertTrue(playersToShowCards.isEmpty());

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class, CardsShuffledEvent.class,
                HandDealtEvent.class, PotCreatedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                PotAmountIncreasedEvent.class, PotAmountIncreasedEvent.class,
                RoundCompletedEvent.class, WinnersDeterminedEvent.class,
                HandCompletedEvent.class);
    }

}
