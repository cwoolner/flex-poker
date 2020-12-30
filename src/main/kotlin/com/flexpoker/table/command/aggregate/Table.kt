package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardsUsedInHand
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.PlayerAddedEvent
import com.flexpoker.table.command.events.PlayerBustedTableEvent
import com.flexpoker.table.command.events.PlayerRemovedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TablePausedEvent
import com.flexpoker.table.command.events.TableResumedEvent
import com.flexpoker.util.toPMap
import com.flexpoker.util.toPSet
import com.flexpoker.util.toPVector
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import java.util.UUID
import java.util.function.Consumer

class Table(creatingFromEvents: Boolean, var state: TableState) {
    private val newEvents = ArrayList<TableEvent>()
    private val appliedEvents = ArrayList<TableEvent>()

    init {
        state = state.copy(
            chipsInBack = state.seatMap.values.filterNotNull().associateWith { state.startingNumberOfChips }.toPMap())
        if (!creatingFromEvents) {
            val tableCreatedEvent = TableCreatedEvent(state.aggregateId, state.gameId, state.seatMap.size,
                state.seatMap, state.startingNumberOfChips)
            newEvents.add(tableCreatedEvent)
            applyCommonEvent(tableCreatedEvent)
        }
    }

    fun fetchNewEvents(): List<TableEvent> {
        return ArrayList(newEvents)
    }

    fun fetchAppliedEvents(): List<TableEvent> {
        return ArrayList(appliedEvents)
    }

    fun applyAllHistoricalEvents(events: List<TableEvent>) {
        events.forEach(Consumer { x: TableEvent -> applyCommonEvent(x) })
    }

    private fun applyCommonEvent(event: TableEvent) {
        state = applyEvent(state, event)
        appliedEvents.add(event)
    }

    fun startNewHandForNewGame(smallBlind: Int, bigBlind: Int, shuffledDeckOfCards: List<Card>,
                               cardsUsedInHand: CardsUsedInHand, handEvaluations: Map<PocketCards, HandEvaluation>) {
        state = state.copy(buttonOnPosition = assignButtonOnForNewGame())
        state = state.copy(smallBlindPosition = assignSmallBlindForNewGame())
        state = state.copy(bigBlindPosition = assignBigBlindForNewGame())
        performNewHandCommonLogic(smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
            handEvaluations, assignActionOnForNewHand())
    }

    fun startNewHandForExistingTable(smallBlind: Int, bigBlind: Int, shuffledDeckOfCards: List<Card>,
                                     cardsUsedInHand: CardsUsedInHand, handEvaluations: Map<PocketCards, HandEvaluation>) {
        state = state.copy(buttonOnPosition = assignButtonOnForNewHand())
        state = state.copy(smallBlindPosition = assignSmallBlindForNewHand())
        state = state.copy(bigBlindPosition = assignBigBlindForNewHand())
        performNewHandCommonLogic(smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
            handEvaluations, assignActionOnForNewHand())
    }

    private fun assignButtonOnForNewGame(): Int {
        while (true) {
            val potentialButtonOnPosition = Random().nextInt(state.seatMap.size)
            if (state.seatMap[Integer.valueOf(potentialButtonOnPosition)] != null) {
                return potentialButtonOnPosition
            }
        }
    }

    private fun assignSmallBlindForNewGame(): Int {
        return if (numberOfPlayersAtTable == 2) state.buttonOnPosition else findNextFilledSeat(state.buttonOnPosition)
    }

    private fun assignBigBlindForNewGame(): Int {
        return if (numberOfPlayersAtTable == 2) findNextFilledSeat(state.buttonOnPosition) else findNextFilledSeat(state.smallBlindPosition)
    }

    private fun assignActionOnForNewHand(): Int {
        return findNextFilledSeat(state.bigBlindPosition)
    }

    private fun assignBigBlindForNewHand(): Int {
        return findNextFilledSeat(state.bigBlindPosition)
    }

    private fun assignSmallBlindForNewHand(): Int {
        return findNextFilledSeat(state.smallBlindPosition)
    }

    private fun assignButtonOnForNewHand(): Int {
        return findNextFilledSeat(state.buttonOnPosition)
    }

    private fun findNextFilledSeat(startingPosition: Int): Int {
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

    private fun performNewHandCommonLogic(smallBlind: Int, bigBlind: Int, shuffledDeckOfCards: List<Card>,
                                          cardsUsedInHand: CardsUsedInHand,
                                          handEvaluations: Map<PocketCards, HandEvaluation>, actionOnPosition: Int) {
        val cardsShuffledEvent = CardsShuffledEvent(state.aggregateId, state.gameId, shuffledDeckOfCards)
        newEvents.add(cardsShuffledEvent)
        applyCommonEvent(cardsShuffledEvent)
        var nextToReceivePocketCards = findNextFilledSeat(state.buttonOnPosition)
        val playerToPocketCardsMap = HashMap<UUID, PocketCards>()
        for (pocketCards in cardsUsedInHand.pocketCards) {
            val playerIdAtPosition = state.seatMap[nextToReceivePocketCards]!!
            playerToPocketCardsMap[playerIdAtPosition] = pocketCards
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards)
            handEvaluations[pocketCards]!!.playerId = playerIdAtPosition
        }
        val playersStillInHand = state.seatMap.values.filterNotNull().toPSet()
        val possibleSeatActionsMap = playersStillInHand.associateWith { emptySet<PlayerAction>() }.toPMap()

        val handState = HandState(state.gameId, state.aggregateId, UUID.randomUUID(), state.seatMap,
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard,
            cardsUsedInHand.riverCard, state.buttonOnPosition, state.smallBlindPosition,
            state.bigBlindPosition, null, playerToPocketCardsMap.toPMap(),
            possibleSeatActionsMap, playersStillInHand, handEvaluations.values.toPVector(),
            HandDealerState.NONE, state.chipsInBack, HashTreePMap.empty(), HashTreePMap.empty(),
            HashTreePMap.empty(), smallBlind, bigBlind, 0, null,
            HashTreePSet.empty(), false, false, false, HashTreePSet.empty())
        val hand = Hand(handState)
        val eventsCreated = hand.dealHand(actionOnPosition)
        eventsCreated.forEach {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    val numberOfPlayersAtTable: Int
        get() = state.seatMap.values.filterNotNull().count()

    fun check(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerCheckedEvent = state.currentHand!!.check(playerId!!, false)
        newEvents.add(playerCheckedEvent)
        applyCommonEvent(playerCheckedEvent)
        handleEndOfRound()
    }

    fun call(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerCalledEvent = state.currentHand!!.call(playerId!!)
        newEvents.add(playerCalledEvent)
        applyCommonEvent(playerCalledEvent)
        handleEndOfRound()
    }

    fun fold(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerFoldedEvent = state.currentHand!!.fold(playerId!!, false)
        newEvents.add(playerFoldedEvent)
        applyCommonEvent(playerFoldedEvent)
        handleEndOfRound()
    }

    fun raise(playerId: UUID?, raiseToAmount: Int) {
        checkHandIsBeingPlayed()
        val playerRaisedEvent = state.currentHand!!.raise(playerId!!, raiseToAmount)
        newEvents.add(playerRaisedEvent)
        applyCommonEvent(playerRaisedEvent)
        changeActionOnIfAppropriate()
    }

    fun expireActionOn(handId: UUID?, playerId: UUID?) {
        checkHandIsBeingPlayed()
        if (!state.currentHand!!.idMatches(handId!!)) {
            return
        }
        val forcedActionOnExpiredEvent = state.currentHand!!.expireActionOn(playerId!!)
        newEvents.add(forcedActionOnExpiredEvent)
        applyCommonEvent(forcedActionOnExpiredEvent)
        handleEndOfRound()
    }

    fun autoMoveHandForward() {
        checkHandIsBeingPlayed()
        handleEndOfRound()
    }

    private fun handleEndOfRound() {
        handlePotAndRoundCompleted()
        changeActionOnIfAppropriate()
        dealCommonCardsIfAppropriate()
        determineWinnersIfAppropriate()
        removeAnyBustedPlayers()
        finishHandIfAppropriate()
    }

    private fun checkHandIsBeingPlayed() {
        if (state.currentHand == null) {
            throw FlexPokerException("no hand in progress")
        }
    }

    private fun handlePotAndRoundCompleted() {
        val endOfRoundEvents = state.currentHand!!.handlePotAndRoundCompleted()
        endOfRoundEvents.forEach {
            newEvents.add(it)
            // TODO: not using applyCommonEvent() here because PotHandler is too
            // stateful and the events get applied to the state down there. when
            // that's refactored, this should change
            appliedEvents.add(it)
        }
    }

    private fun changeActionOnIfAppropriate() {
        val actionOnChangedEvents = state.currentHand!!.changeActionOn()
        actionOnChangedEvents.forEach {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun dealCommonCardsIfAppropriate() {
        state.currentHand!!.dealCommonCardsIfAppropriate().ifPresent {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun determineWinnersIfAppropriate() {
        state.currentHand!!.determineWinnersIfAppropriate().ifPresent {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun removeAnyBustedPlayers() {
        state.chipsInBack.filterValues { it == 0 }.forEach {
            val event = PlayerBustedTableEvent(state.aggregateId, state.gameId, it.key)
            newEvents.add(event)
            applyCommonEvent(event)
        }
    }

    private fun finishHandIfAppropriate() {
        val handCompleteEvent = state.currentHand!!.finishHandIfAppropriate()
        if (handCompleteEvent.isPresent) {
            newEvents.add(handCompleteEvent.get())
            applyCommonEvent(handCompleteEvent.get())
        } else {
            state.currentHand!!.autoMoveHandForward().ifPresent {
                newEvents.add(it)
                applyCommonEvent(it)
            }
        }
    }

    fun addPlayer(playerId: UUID?, chips: Int) {
        if (state.seatMap.values.contains(playerId)) {
            throw FlexPokerException("player already at this table")
        }
        val newPlayerPosition = findRandomOpenSeat()
        val playerAddedEvent = PlayerAddedEvent(state.aggregateId, state.gameId, playerId!!, chips, newPlayerPosition)
        newEvents.add(playerAddedEvent)
        applyCommonEvent(playerAddedEvent)
    }

    fun removePlayer(playerId: UUID?) {
        if (!state.seatMap.values.contains(playerId)) {
            throw FlexPokerException("player not at this table")
        }
        if (state.currentHand != null) {
            throw FlexPokerException("can't remove a player while in a hand")
        }
        val playerRemovedEvent = PlayerRemovedEvent(state.aggregateId, state.gameId, playerId!!)
        newEvents.add(playerRemovedEvent)
        applyCommonEvent(playerRemovedEvent)
    }

    fun pause() {
        if (state.paused) {
            throw FlexPokerException("table is already paused.  can't pause again.")
        }
        val tablePausedEvent = TablePausedEvent(state.aggregateId, state.gameId)
        newEvents.add(tablePausedEvent)
        applyCommonEvent(tablePausedEvent)
    }

    fun resume() {
        if (!state.paused) {
            throw FlexPokerException("table is not paused.  can't resume.")
        }
        val tableResumedEvent = TableResumedEvent(state.aggregateId, state.gameId)
        newEvents.add(tableResumedEvent)
        applyCommonEvent(tableResumedEvent)
    }

    private fun findRandomOpenSeat(): Int {
        while (true) {
            val potentialNewPlayerPosition = Random().nextInt(state.seatMap.size)
            if (state.seatMap[potentialNewPlayerPosition] == null) {
                return potentialNewPlayerPosition
            }
        }
    }

}