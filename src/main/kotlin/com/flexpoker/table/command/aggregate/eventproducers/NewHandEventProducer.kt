package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardsUsedInHand
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.aggregate.HandDealerState
import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.RandomNumberGenerator
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvent
import com.flexpoker.table.command.aggregate.numberOfPlayersAtTable
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.util.toPMap
import com.flexpoker.util.toPSet
import com.flexpoker.util.toPVector
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import java.util.UUID

fun startNewHandForNewGame(state: TableState, smallBlind: Int, bigBlind: Int, shuffledDeckOfCards: List<Card>,
                           cardsUsedInHand: CardsUsedInHand, handEvaluations: Map<PocketCards, HandEvaluation>,
                           randomNumberGenerator: RandomNumberGenerator
): List<TableEvent> {
    var updatedState = state
    updatedState = updatedState.copy(buttonOnPosition = assignButtonOnForNewGame(updatedState, randomNumberGenerator))
    updatedState = updatedState.copy(smallBlindPosition = assignSmallBlindForNewGame(updatedState))
    updatedState = updatedState.copy(bigBlindPosition = assignBigBlindForNewGame(updatedState))
    return performNewHandCommonLogic(updatedState, smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
        handEvaluations, assignActionOnForNewHand(updatedState))
}

fun startNewHandForExistingTable(state: TableState, smallBlind: Int, bigBlind: Int, shuffledDeckOfCards: List<Card>,
                                 cardsUsedInHand: CardsUsedInHand, handEvaluations: Map<PocketCards, HandEvaluation>
): List<TableEvent> {
    var updatedState = state
    updatedState = updatedState.copy(buttonOnPosition = assignButtonOnForNewHand(updatedState))
    updatedState = updatedState.copy(smallBlindPosition = assignSmallBlindForNewHand(updatedState))
    updatedState = updatedState.copy(bigBlindPosition = assignBigBlindForNewHand(updatedState))
    return performNewHandCommonLogic(updatedState, smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
        handEvaluations, assignActionOnForNewHand(updatedState))
}

private fun assignButtonOnForNewGame(state: TableState, randomNumberGenerator: RandomNumberGenerator): Int {
    while (true) {
        val potentialButtonOnPosition = randomNumberGenerator.int(state.seatMap.size)
        if (state.seatMap[potentialButtonOnPosition] != null) {
            return potentialButtonOnPosition
        }
    }
}

private fun assignSmallBlindForNewGame(state: TableState): Int {
    return if (numberOfPlayersAtTable(state) == 2) state.buttonOnPosition
    else findNextFilledSeat(state, state.buttonOnPosition)
}

private fun assignBigBlindForNewGame(state: TableState): Int {
    return if (numberOfPlayersAtTable(state) == 2) findNextFilledSeat(state, state.buttonOnPosition)
    else findNextFilledSeat(state, state.smallBlindPosition)
}

private fun assignActionOnForNewHand(state: TableState): Int {
    return findNextFilledSeat(state, state.bigBlindPosition)
}

private fun assignBigBlindForNewHand(state: TableState): Int {
    return findNextFilledSeat(state, state.bigBlindPosition)
}

private fun assignSmallBlindForNewHand(state: TableState): Int {
    return findNextFilledSeat(state, state.smallBlindPosition)
}

private fun assignButtonOnForNewHand(state: TableState): Int {
    return findNextFilledSeat(state, state.buttonOnPosition)
}

private fun findNextFilledSeat(state: TableState, startingPosition: Int): Int {
    for (i in startingPosition + 1 until state.seatMap.size) {
        if (state.seatMap[i] != null) {
            return i
        }
    }
    for (i in 0 until startingPosition) {
        if (state.seatMap[i] != null) {
            return i
        }
    }
    throw IllegalStateException("unable to find next filled seat")
}

private fun performNewHandCommonLogic(state: TableState, smallBlind: Int, bigBlind: Int,
                                      shuffledDeckOfCards: List<Card>, cardsUsedInHand: CardsUsedInHand,
                                      handEvaluations: Map<PocketCards, HandEvaluation>, actionOnPosition: Int
): List<TableEvent> {
    var updatedState = state
    val cardsShuffledEvent = CardsShuffledEvent(updatedState.aggregateId, updatedState.gameId, shuffledDeckOfCards)
    updatedState = applyEvent(updatedState, cardsShuffledEvent)
    var nextToReceivePocketCards = findNextFilledSeat(updatedState, updatedState.buttonOnPosition)
    val playerToPocketCardsMap = HashMap<UUID, PocketCards>()
    for (pocketCards in cardsUsedInHand.pocketCards) {
        val playerIdAtPosition = updatedState.seatMap[nextToReceivePocketCards]!!
        playerToPocketCardsMap[playerIdAtPosition] = pocketCards
        nextToReceivePocketCards = findNextFilledSeat(updatedState, nextToReceivePocketCards)
        handEvaluations[pocketCards]!!.playerId = playerIdAtPosition
    }
    val playersStillInHand = updatedState.seatMap.values.filterNotNull().toPSet()
    val possibleSeatActionsMap = playersStillInHand.associateWith { emptySet<PlayerAction>() }.toPMap()

    val handState = HandState(updatedState.gameId, updatedState.aggregateId, UUID.randomUUID(), updatedState.seatMap,
        cardsUsedInHand.flopCards, cardsUsedInHand.turnCard,
        cardsUsedInHand.riverCard, updatedState.buttonOnPosition, updatedState.smallBlindPosition,
        updatedState.bigBlindPosition, null, playerToPocketCardsMap.toPMap(),
        possibleSeatActionsMap, playersStillInHand, handEvaluations.values.toPVector(),
        HandDealerState.NONE, updatedState.chipsInBack, HashTreePMap.empty(), HashTreePMap.empty(),
        HashTreePMap.empty(), smallBlind, bigBlind, 0, null,
        HashTreePSet.empty(), false, false, false, HashTreePSet.empty())

    return listOf(cardsShuffledEvent) + dealHand(handState, actionOnPosition)
}
