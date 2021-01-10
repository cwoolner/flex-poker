package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun changeActionOn(state: HandState): List<TableEvent> {
    if (state.chipsInBackMap.values.all { it == 0 }) {
        return emptyList()
    }
    val eventsCreated = ArrayList<TableEvent>()

    // do not change action on if the hand is over. starting a new hand
    // should adjust that
    if (state.handDealerState === HandDealerState.COMPLETE) {
        return eventsCreated
    }

    // the player just bet, so a new last to act needs to be determined
    when (state.seatMap[state.actionOnPosition]) {
        state.originatingBettorPlayerId -> {
            val nextPlayerToAct = findNextToAct(state)
            val lastPlayerToAct = state.seatMap[determineLastToAct(state)]
            val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
            val lastToActChangedEvent = LastToActChangedEvent(state.tableId, state.gameId, state.entityId, lastPlayerToAct!!)
            eventsCreated.add(actionOnChangedEvent)
            eventsCreated.add(lastToActChangedEvent)
        }
        state.lastToActPlayerId -> {
            val nextPlayerToAct = findActionOnPlayerIdForNewRound(state)
            val newRoundLastPlayerToAct = state.seatMap[Integer.valueOf(determineLastToAct(state))]
            val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
            val lastToActChangedEvent = LastToActChangedEvent(state.tableId, state.gameId, state.entityId, newRoundLastPlayerToAct!!)
            eventsCreated.add(actionOnChangedEvent)
            eventsCreated.add(lastToActChangedEvent)
        }
        else -> {
            val nextPlayerToAct = findNextToAct(state)
            val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
            eventsCreated.add(actionOnChangedEvent)
        }
    }
    return eventsCreated
}

private fun findNextToAct(state: HandState): UUID {
    for (i in state.actionOnPosition + 1 until state.seatMap.size) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null && state.playersStillInHand.contains(playerAtTable)) {
            return playerAtTable
        }
    }
    for (i in 0 until state.actionOnPosition) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null && state.playersStillInHand.contains(playerAtTable)) {
            return playerAtTable
        }
    }
    throw IllegalStateException("unable to find next to act")
}

private fun determineLastToAct(state: HandState): Int {
    var seatIndex: Int
    if (state.originatingBettorPlayerId == null) {
        seatIndex = state.buttonOnPosition
    } else {
        seatIndex = state.seatMap.entries.first { it.value == state.originatingBettorPlayerId }.key
        if (seatIndex == 0) {
            seatIndex = state.seatMap.size - 1
        } else {
            seatIndex--
        }
    }
    for (i in seatIndex downTo 0) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null && state.playersStillInHand.contains(playerAtTable)) {
            return i
        }
    }
    for (i in state.seatMap.size - 1 downTo seatIndex + 1) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null && state.playersStillInHand.contains(playerAtTable)) {
            return i
        }
    }
    throw IllegalStateException("unable to determine last to act")
}

private fun findActionOnPlayerIdForNewRound(state: HandState): UUID {
    val buttonIndex = state.buttonOnPosition
    for (i in buttonIndex + 1 until state.seatMap.size) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null
            && state.playersStillInHand.contains(playerAtTable)
            && state.chipsInBackMap[playerAtTable] != 0) {
            return playerAtTable
        }
    }
    for (i in 0 until buttonIndex) {
        val playerAtTable = state.seatMap[i]
        if (playerAtTable != null
            && state.playersStillInHand.contains(playerAtTable)
            && state.chipsInBackMap[playerAtTable] != 0) {
            return playerAtTable
        }
    }
    throw FlexPokerException("couldn't determine new action on after round")
}