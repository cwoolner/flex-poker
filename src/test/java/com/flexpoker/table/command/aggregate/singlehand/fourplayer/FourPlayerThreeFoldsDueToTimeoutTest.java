package com.flexpoker.table.command.aggregate.singlehand.fourplayer;

import static com.flexpoker.table.command.framework.TableEventType.ActionOnChanged;
import static com.flexpoker.table.command.framework.TableEventType.CardsShuffled;
import static com.flexpoker.table.command.framework.TableEventType.HandCompleted;
import static com.flexpoker.table.command.framework.TableEventType.HandDealtEvent;
import static com.flexpoker.table.command.framework.TableEventType.PlayerFolded;
import static com.flexpoker.table.command.framework.TableEventType.PotAmountIncreased;
import static com.flexpoker.table.command.framework.TableEventType.PotCreated;
import static com.flexpoker.table.command.framework.TableEventType.RoundCompleted;
import static com.flexpoker.table.command.framework.TableEventType.TableCreated;
import static com.flexpoker.table.command.framework.TableEventType.WinnersDetermined;
import static com.flexpoker.test.util.CommonAssertions.verifyEventIdsAndVersionNumbers;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class FourPlayerThreeFoldsDueToTimeoutTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        UUID player4Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, player1Id, player2Id,
                player3Id, player4Id);

        ActionOnChangedEvent rightOfButtonActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(3);
        table.expireActionOn(rightOfButtonActionOnChangedEvent.getHandId(),
                rightOfButtonActionOnChangedEvent.getPlayerId());

        ActionOnChangedEvent buttonActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(5);
        table.expireActionOn(buttonActionOnChangedEvent.getHandId(),
                buttonActionOnChangedEvent.getPlayerId());

        ActionOnChangedEvent smallBlindActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(7);
        table.expireActionOn(smallBlindActionOnChangedEvent.getHandId(),
                smallBlindActionOnChangedEvent.getPlayerId());
        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(newEvents, TableCreated, CardsShuffled,
                HandDealtEvent, ActionOnChanged, PlayerFolded, ActionOnChanged,
                PlayerFolded, ActionOnChanged, PlayerFolded, PotCreated,
                PotAmountIncreased, PotAmountIncreased, RoundCompleted,
                WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }


}
