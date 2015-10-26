package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static com.flexpoker.table.command.framework.TableEventType.ActionOnChanged;
import static com.flexpoker.table.command.framework.TableEventType.CardsShuffled;
import static com.flexpoker.table.command.framework.TableEventType.FlopCardsDealt;
import static com.flexpoker.table.command.framework.TableEventType.HandCompleted;
import static com.flexpoker.table.command.framework.TableEventType.HandDealtEvent;
import static com.flexpoker.table.command.framework.TableEventType.LastToActChanged;
import static com.flexpoker.table.command.framework.TableEventType.PlayerCalled;
import static com.flexpoker.table.command.framework.TableEventType.PlayerChecked;
import static com.flexpoker.table.command.framework.TableEventType.PotAmountIncreased;
import static com.flexpoker.table.command.framework.TableEventType.PotCreated;
import static com.flexpoker.table.command.framework.TableEventType.RiverCardDealt;
import static com.flexpoker.table.command.framework.TableEventType.RoundCompleted;
import static com.flexpoker.table.command.framework.TableEventType.TableCreated;
import static com.flexpoker.table.command.framework.TableEventType.TurnCardDealt;
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

public class TwoPlayerSmallBlindCallAndBigBlindCheckDueToTimeoutTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, UUID.randomUUID(),
                UUID.randomUUID());

        // use the info in action on event to simulate the expire
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(3)).getPlayerId();
        table.call(smallBlindAndButtonPlayerId);

        ActionOnChangedEvent actionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(5);
        UUID bigBlindPlayerId = actionOnChangedEvent.getPlayerId();
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

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(
                newEvents,
                TableCreated,
                CardsShuffled,
                HandDealtEvent,
                ActionOnChanged,
                // pre-flop
                PlayerCalled, ActionOnChanged, PlayerChecked, PotCreated,
                PotAmountIncreased, PotAmountIncreased, RoundCompleted,
                ActionOnChanged,
                LastToActChanged,
                FlopCardsDealt,
                // post-flop
                PlayerChecked, ActionOnChanged, PlayerChecked, RoundCompleted,
                ActionOnChanged, LastToActChanged,
                TurnCardDealt,
                // post-turn
                PlayerChecked, ActionOnChanged, PlayerChecked, RoundCompleted,
                ActionOnChanged, LastToActChanged, RiverCardDealt,
                // post-river
                PlayerChecked, ActionOnChanged, PlayerChecked, RoundCompleted,
                WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

}
