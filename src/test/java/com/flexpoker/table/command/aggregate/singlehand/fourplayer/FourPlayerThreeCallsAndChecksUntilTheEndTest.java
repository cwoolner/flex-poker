package com.flexpoker.table.command.aggregate.singlehand.fourplayer;

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
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;

public class FourPlayerThreeCallsAndChecksUntilTheEndTest {

    @Test
    void test() {
        var tableId = UUID.randomUUID();
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var player4Id = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id, player4Id);

        var rightOfButtonOnPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(4)).getPlayerId();
        table.call(rightOfButtonOnPlayerId);

        var buttonOnPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(6)).getPlayerId();
        table.call(buttonOnPlayerId);

        var smallBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(8)).getPlayerId();
        table.call(smallBlindPlayerId);

        var bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(10)).getPlayerId();
        table.check(bigBlindPlayerId);

        // post-flop
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(rightOfButtonOnPlayerId);
        table.check(buttonOnPlayerId);

        // post-turn
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(rightOfButtonOnPlayerId);
        table.check(buttonOnPlayerId);

        // post-river
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(rightOfButtonOnPlayerId);
        table.check(buttonOnPlayerId);

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class,
                CardsShuffledEvent.class,
                HandDealtEvent.class,
                PotCreatedEvent.class,
                ActionOnChangedEvent.class,
                // pre-flop
                PlayerCalledEvent.class, ActionOnChangedEvent.class,
                PlayerCalledEvent.class, ActionOnChangedEvent.class,
                PlayerCalledEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class,
                PotAmountIncreasedEvent.class,
                RoundCompletedEvent.class,
                ActionOnChangedEvent.class,
                LastToActChangedEvent.class,
                FlopCardsDealtEvent.class,
                // post-flop
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                ActionOnChangedEvent.class,
                LastToActChangedEvent.class,
                TurnCardDealtEvent.class,
                // post-turn
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                ActionOnChangedEvent.class, LastToActChangedEvent.class,
                RiverCardDealtEvent.class,
                // post-river
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, ActionOnChangedEvent.class,
                PlayerCheckedEvent.class, RoundCompletedEvent.class,
                WinnersDeterminedEvent.class, HandCompletedEvent.class);
    }
}
