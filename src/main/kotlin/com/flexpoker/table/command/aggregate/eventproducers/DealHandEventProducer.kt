package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import org.pcollections.HashTreePSet
import java.util.UUID

fun dealHand(state: HandState, actionOnPosition: Int): List<TableEvent> {
    var updatedState = state
    updatedState.seatMap.keys.filter { updatedState.seatMap[it] != null }.forEach {
        updatedState = handleStartOfHandPlayerValues(updatedState, it)
    }
    updatedState = updatedState.copy(
        lastToActPlayerId = updatedState.seatMap[updatedState.bigBlindPosition],
        handDealerState = HandDealerState.POCKET_CARDS_DEALT
    )
    val handDealtEvent = HandDealtEvent(updatedState.tableId, updatedState.gameId, updatedState.entityId,
        updatedState.flopCards, updatedState.turnCard, updatedState.riverCard, updatedState.buttonOnPosition,
        updatedState.smallBlindPosition, updatedState.bigBlindPosition, updatedState.lastToActPlayerId!!,
        updatedState.seatMap, updatedState.playerToPocketCardsMap, updatedState.possibleSeatActionsMap,
        updatedState.playersStillInHand, updatedState.handEvaluationList, updatedState.handDealerState,
        updatedState.chipsInBackMap, updatedState.chipsInFrontMap, updatedState.callAmountsMap,
        updatedState.raiseToAmountsMap, updatedState.smallBlind, updatedState.bigBlind)
    val potCreatedEvent = PotCreatedEvent(updatedState.tableId, updatedState.gameId, updatedState.entityId, UUID.randomUUID(), updatedState.playersStillInHand)
    val actionOnChangedEvent = ActionOnChangedEvent(updatedState.tableId, updatedState.gameId, updatedState.entityId, updatedState.seatMap[actionOnPosition]!!)
    return listOf(handDealtEvent, potCreatedEvent, actionOnChangedEvent)
}

fun handleStartOfHandPlayerValues(state: HandState, seatPosition: Int): HandState {
    var updatedState = state
    val playerId = updatedState.seatMap[seatPosition]!!
    var chipsInFront = 0
    var callAmount = updatedState.bigBlind
    val raiseToAmount = updatedState.bigBlind * 2
    if (seatPosition == updatedState.bigBlindPosition) {
        chipsInFront = updatedState.bigBlind
        callAmount = 0
    } else if (seatPosition == updatedState.smallBlindPosition) {
        chipsInFront = updatedState.smallBlind
        callAmount = updatedState.smallBlind
    }
    updatedState = updatedState.copy(
        chipsInFrontMap =
        if (chipsInFront > updatedState.chipsInBackMap[playerId]!!)
            updatedState.chipsInFrontMap.plus(playerId, updatedState.chipsInBackMap[playerId])
        else
            updatedState.chipsInFrontMap.plus(playerId, chipsInFront)
    )
    updatedState = subtractFromChipsInBack(updatedState, playerId, updatedState.chipsInFrontMap[playerId]!!)
    updatedState = updatedState.copy(
        callAmountsMap =
        if (callAmount > updatedState.chipsInBackMap[playerId]!!)
            updatedState.callAmountsMap.plus(playerId, updatedState.chipsInBackMap[playerId])
        else
            updatedState.callAmountsMap.plus(playerId, callAmount)
    )
    val totalChips = updatedState.chipsInBackMap[playerId]!! + updatedState.chipsInFrontMap[playerId]!!
    updatedState = updatedState.copy(
        raiseToAmountsMap =
        if (raiseToAmount > totalChips)
            updatedState.raiseToAmountsMap.plus(playerId, totalChips)
        else
            updatedState.raiseToAmountsMap.plus(playerId, raiseToAmount))

    if (updatedState.raiseToAmountsMap[playerId]!! > 0) {
        updatedState = updatedState.copy(possibleSeatActionsMap = updatedState.possibleSeatActionsMap.plus(playerId,
            HashTreePSet.singleton(PlayerAction.RAISE)))
    }
    updatedState = if (updatedState.callAmountsMap[playerId]!! > 0) {
        val playerActions = updatedState.possibleSeatActionsMap[playerId]!!.plus(setOf(PlayerAction.CALL, PlayerAction.FOLD))
        updatedState.copy(possibleSeatActionsMap = updatedState.possibleSeatActionsMap.plus(playerId, playerActions))
    } else {
        val playerActions = updatedState.possibleSeatActionsMap[playerId]!!.plus(PlayerAction.CHECK)
        updatedState.copy(possibleSeatActionsMap = updatedState.possibleSeatActionsMap.plus(playerId, playerActions))
    }
    return updatedState
}

private fun subtractFromChipsInBack(state: HandState, playerId: UUID, chipsToSubtract: Int): HandState {
    val currentAmount = state.chipsInBackMap[playerId]!!
    return state.copy(chipsInBackMap = state.chipsInBackMap.plus(playerId, currentAmount - chipsToSubtract))
}