package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;

public class TwoPlayerSmallBlindAllInBigBlindCalls {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(),
                UUID.randomUUID());

        // use the info in action on event to get the player id of the small
        // blind
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(4)).getPlayerId();
        table.raise(smallBlindAndButtonPlayerId, 1500);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(6))
                .getPlayerId();
        table.call(bigBlindPlayerId);

        UUID handId = ((HandDealtEvent) table.fetchNewEvents().get(2)).getHandId();
        table.autoMoveHandForward(handId);
        table.autoMoveHandForward(handId);
        table.autoMoveHandForward(handId);

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class,
                CardsShuffledEvent.class,
                HandDealtEvent.class,
                PotCreatedEvent.class,
                ActionOnChangedEvent.class,
                // pre-flop
                PlayerRaisedEvent.class, ActionOnChangedEvent.class,
                LastToActChangedEvent.class,
                PlayerCalledEvent.class,
                PotAmountIncreasedEvent.class,
                PotClosedEvent.class,
                RoundCompletedEvent.class,
                FlopCardsDealtEvent.class, AutoMoveHandForwardEvent.class, RoundCompletedEvent.class,
                // post-flop
                TurnCardDealtEvent.class, AutoMoveHandForwardEvent.class, RoundCompletedEvent.class,
                // post-turn
                RiverCardDealtEvent.class, AutoMoveHandForwardEvent.class, RoundCompletedEvent.class,
                // post-river
                WinnersDeterminedEvent.class, HandCompletedEvent.class);
    }

}
