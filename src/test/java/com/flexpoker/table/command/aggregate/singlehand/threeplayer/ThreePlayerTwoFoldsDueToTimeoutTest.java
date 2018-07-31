package com.flexpoker.table.command.aggregate.singlehand.threeplayer;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.UUID;

import org.junit.jupiter.api.Test;

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

public class ThreePlayerTwoFoldsDueToTimeoutTest {

    @Test
    void test() {
        var tableId = UUID.randomUUID();
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id);

        var buttonActionOnChangedEvent = (ActionOnChangedEvent) table.fetchNewEvents().get(4);
        table.expireActionOn(buttonActionOnChangedEvent.getHandId(), buttonActionOnChangedEvent.getPlayerId());

        var smallBlindActionOnChangedEvent = (ActionOnChangedEvent) table.fetchNewEvents().get(6);
        table.expireActionOn(smallBlindActionOnChangedEvent.getHandId(), smallBlindActionOnChangedEvent.getPlayerId());

        verifyAppliedAndNewEventsForAggregate(table,
                TableCreatedEvent.class, CardsShuffledEvent.class,
                HandDealtEvent.class, PotCreatedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                ActionOnChangedEvent.class, PlayerForceFoldedEvent.class,
                PotAmountIncreasedEvent.class, PotAmountIncreasedEvent.class,
                RoundCompletedEvent.class, WinnersDeterminedEvent.class,
                HandCompletedEvent.class);
    }


}
