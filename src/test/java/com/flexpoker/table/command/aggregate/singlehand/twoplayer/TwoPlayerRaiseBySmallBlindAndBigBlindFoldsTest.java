package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.test.util.CommonAssertions.verifyEventIdsAndVersionNumbers;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class TwoPlayerRaiseBySmallBlindAndBigBlindFoldsTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(),
                UUID.randomUUID());

        // use the info in action on event to determine who the small
        // blind/button is on
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(4)).getPlayerId();
        table.raise(smallBlindAndButtonPlayerId, 40);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(6))
                .getPlayerId();
        table.fold(bigBlindPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(newEvents,
                TableCreatedEvent.class, CardsShuffledEvent.class,
                HandDealtEvent.class, PotCreatedEvent.class,
                ActionOnChangedEvent.class, PlayerRaisedEvent.class,
                ActionOnChangedEvent.class, LastToActChangedEvent.class,
                PlayerFoldedEvent.class, PotAmountIncreasedEvent.class,
                PotAmountIncreasedEvent.class, RoundCompletedEvent.class,
                WinnersDeterminedEvent.class, HandCompletedEvent.class);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

}
