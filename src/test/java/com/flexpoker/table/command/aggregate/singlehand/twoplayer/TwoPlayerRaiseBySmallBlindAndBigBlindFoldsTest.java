package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.table.command.framework.TableEventType.ActionOnChanged;
import static com.flexpoker.table.command.framework.TableEventType.CardsShuffled;
import static com.flexpoker.table.command.framework.TableEventType.HandCompleted;
import static com.flexpoker.table.command.framework.TableEventType.HandDealtEvent;
import static com.flexpoker.table.command.framework.TableEventType.LastToActChanged;
import static com.flexpoker.table.command.framework.TableEventType.PlayerFolded;
import static com.flexpoker.table.command.framework.TableEventType.PlayerRaised;
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

public class TwoPlayerRaiseBySmallBlindAndBigBlindFoldsTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, UUID.randomUUID(),
                UUID.randomUUID());

        // use the info in action on event to determine who the small
        // blind/button is on
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(3)).getPlayerId();
        table.raise(smallBlindAndButtonPlayerId, 40);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(5))
                .getPlayerId();
        table.fold(bigBlindPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(newEvents, TableCreated, CardsShuffled,
                HandDealtEvent, ActionOnChanged, PlayerRaised, ActionOnChanged,
                LastToActChanged, PlayerFolded, PotCreated, PotAmountIncreased,
                PotAmountIncreased, RoundCompleted, WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

}
