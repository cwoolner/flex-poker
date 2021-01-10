package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.fetchChipsWon
import com.flexpoker.table.command.aggregate.fetchPlayersRequiredToShowCards
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent

fun determineWinnersIfAppropriate(state: HandState): List<TableEvent> {
    return if (state.handDealerState === HandDealerState.COMPLETE) {
        val playersRequiredToShowCards = fetchPlayersRequiredToShowCards(state.pots, state.playersStillInHand)
        val playersToChipsWonMap = fetchChipsWon(state.pots, state.playersStillInHand)
        listOf(WinnersDeterminedEvent(state.tableId, state.gameId, state.entityId,
            playersRequiredToShowCards, playersToChipsWonMap))
    } else {
        emptyList()
    }
}
