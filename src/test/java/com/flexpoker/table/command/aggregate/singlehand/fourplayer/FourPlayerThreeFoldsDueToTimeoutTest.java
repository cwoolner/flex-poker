package com.flexpoker.table.command.aggregate.singlehand.fourplayer;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.Table;
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

public class FourPlayerThreeFoldsDueToTimeoutTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        UUID player4Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id,
                player3Id, player4Id);

        ActionOnChangedEvent rightOfButtonActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(4);
        table.expireActionOn(rightOfButtonActionOnChangedEvent.getHandId(),
                rightOfButtonActionOnChangedEvent.getPlayerId());

        ActionOnChangedEvent buttonActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(6);
        table.expireActionOn(buttonActionOnChangedEvent.getHandId(),
                buttonActionOnChangedEvent.getPlayerId());

        ActionOnChangedEvent smallBlindActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(8);
        table.expireActionOn(smallBlindActionOnChangedEvent.getHandId(),
                smallBlindActionOnChangedEvent.getPlayerId());

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class, CardsShuffledEvent.class,
                HandDealtEvent.class, PotCreatedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                PotAmountIncreasedEvent.class, PotAmountIncreasedEvent.class,
                RoundCompletedEvent.class, WinnersDeterminedEvent.class,
                HandCompletedEvent.class);
    }


}
