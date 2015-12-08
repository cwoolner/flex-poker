package com.flexpoker.table.command.aggregate.singlehand.threeplayer;

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

public class ThreePlayerTwoCallsAndChecksUntilTheEndTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, player1Id, player2Id,
                player3Id);

        UUID buttonOnPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(3))
                .getPlayerId();
        table.call(buttonOnPlayerId);

        UUID smallBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(5))
                .getPlayerId();
        table.call(smallBlindPlayerId);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(7))
                .getPlayerId();
        table.check(bigBlindPlayerId);

        // post-flop
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        // post-turn
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        // post-river
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(
                newEvents,
                TableCreated,
                CardsShuffled,
                HandDealtEvent,
                ActionOnChanged,
                // pre-flop
                PlayerCalled, ActionOnChanged, PlayerCalled, ActionOnChanged,
                PlayerChecked, PotCreated, PotAmountIncreased,
                PotAmountIncreased,
                RoundCompleted,
                ActionOnChanged,
                LastToActChanged,
                FlopCardsDealt,
                // post-flop
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted,
                ActionOnChanged,
                LastToActChanged,
                TurnCardDealt,
                // post-turn
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted, ActionOnChanged, LastToActChanged,
                RiverCardDealt,
                // post-river
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted, WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }
}
