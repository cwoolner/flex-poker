package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerForceCheckedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;

public class TwoPlayerSmallBlindCallAndBigBlindCheckDueToTimeoutTest {

    @Test
    void test() {
        var tableId = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID());

        // use the info in action on event to simulate the expire
        var smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(4)).getPlayerId();
        table.call(smallBlindAndButtonPlayerId);

        var actionOnChangedEvent = (ActionOnChangedEvent) table.fetchNewEvents().get(6);
        var bigBlindPlayerId = actionOnChangedEvent.getPlayerId();
        table.expireActionOn(actionOnChangedEvent.getHandId(), bigBlindPlayerId);

        // post-flop
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-turn
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-river
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class,
                CardsShuffledEvent.class,
                HandDealtEvent.class,
                PotCreatedEvent.class,
                ActionOnChangedEvent.class,
                // pre-flop
                PlayerCalledEvent.class, ActionOnChangedEvent.class,
                PlayerForceCheckedEvent.class,
                PotAmountIncreasedEvent.class,
                RoundCompletedEvent.class, ActionOnChangedEvent.class,
                LastToActChangedEvent.class, FlopCardsDealtEvent.class,
                // post-flop
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                ActionOnChangedEvent.class, LastToActChangedEvent.class,
                TurnCardDealtEvent.class,
                // post-turn
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                ActionOnChangedEvent.class, LastToActChangedEvent.class,
                RiverCardDealtEvent.class,
                // post-river
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                WinnersDeterminedEvent.class, HandCompletedEvent.class);
    }

}
