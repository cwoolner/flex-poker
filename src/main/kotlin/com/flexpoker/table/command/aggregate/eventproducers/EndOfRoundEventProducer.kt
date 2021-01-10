package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.hand.autoMoveHandForward
import com.flexpoker.table.command.aggregate.eventproducers.hand.changeActionOn
import com.flexpoker.table.command.aggregate.eventproducers.hand.dealCommonCardsIfAppropriate
import com.flexpoker.table.command.aggregate.eventproducers.hand.determineWinnersIfAppropriate
import com.flexpoker.table.command.aggregate.eventproducers.hand.finishHandIfAppropriate
import com.flexpoker.table.command.aggregate.eventproducers.hand.handlePotAndRoundCompleted
import com.flexpoker.table.command.events.PlayerBustedTableEvent
import com.flexpoker.table.command.events.TableEvent

fun handleEndOfRound(state: TableState): List<TableEvent> {
    var updatedState = state

    val potAndRoundCompletedEvents = handlePotAndRoundCompleted(updatedState.currentHand!!)
    updatedState = applyEvents(updatedState, potAndRoundCompletedEvents)
    val changeActionOnEvents = changeActionOn(updatedState.currentHand!!)
    updatedState = applyEvents(updatedState, changeActionOnEvents)
    val dealCommonCardsEvents = dealCommonCardsIfAppropriate(updatedState.currentHand!!)
    updatedState = applyEvents(updatedState, dealCommonCardsEvents)
    val determineWinnersEvents = determineWinnersIfAppropriate(updatedState.currentHand!!)
    updatedState = applyEvents(updatedState, determineWinnersEvents)
    val bustedPlayersEvents = removeAnyBustedPlayers(updatedState)
    updatedState = applyEvents(updatedState, bustedPlayersEvents)
    val finishHandEvents = finishHandIfAppropriate(updatedState)
    updatedState = applyEvents(updatedState, finishHandEvents)

    return potAndRoundCompletedEvents
        .plus(changeActionOnEvents)
        .plus(dealCommonCardsEvents)
        .plus(determineWinnersEvents)
        .plus(bustedPlayersEvents)
        .plus(finishHandEvents)
}

private fun removeAnyBustedPlayers(state: TableState): List<TableEvent> {
    return state.chipsInBack
        .filterValues { it == 0 }
        .map { PlayerBustedTableEvent(state.aggregateId, state.gameId, it.key) }
}

private fun finishHandIfAppropriate(state: TableState): List<TableEvent> {
    val handCompleteEvents = finishHandIfAppropriate(state.currentHand!!)
    return if (handCompleteEvents.isNotEmpty()) {
        handCompleteEvents
    } else {
        autoMoveHandForward(state.currentHand)
    }
}

