package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardsUsedInHand
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerAddedEvent
import com.flexpoker.table.command.events.PlayerBustedTableEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.PlayerRemovedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TablePausedEvent
import com.flexpoker.table.command.events.TableResumedEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.TreePVector
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.Random
import java.util.UUID
import java.util.function.Consumer

class Table(creatingFromEvents: Boolean, var state: TableState) {
    private val newEvents = ArrayList<TableEvent>()
    private val appliedEvents = ArrayList<TableEvent>()

    init {
        state = state.copy(chipsInBack = HashTreePMap.from(state.seatMap.values
            .filterNotNull().associateWith { state.startingNumberOfChips }))
        if (!creatingFromEvents) {
            val tableCreatedEvent = TableCreatedEvent(state.aggregateId, state.gameId, state.seatMap.size,
                HashTreePMap.from(state.seatMap), state.startingNumberOfChips)
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

    private fun applyEvent(event: TableEvent) {
        when (event) {
            is TableCreatedEvent -> { }
            is CardsShuffledEvent -> { }
            is HandDealtEvent -> {
                state = state.copy(
                    buttonOnPosition = event.buttonOnPosition,
                    smallBlindPosition = event.smallBlindPosition,
                    bigBlindPosition = event.bigBlindPosition
                )
                val handState = HandState(
                    event.gameId, event.aggregateId, event.handId, state.seatMap,
                    event.flopCards, event.turnCard, event.riverCard, event.buttonOnPosition,
                    event.smallBlindPosition, event.bigBlindPosition,
                    event.lastToActPlayerId, event.playerToPocketCardsMap,
                    event.possibleSeatActionsMap, event.playersStillInHand,
                    event.handEvaluations, event.handDealerState,
                    event.chipsInBack, event.chipsInFrontMap,
                    event.callAmountsMap, event.raiseToAmountsMap,
                    event.smallBlind, event.bigBlind, 0, null,
                    HashTreePSet.empty(), false, false, false, mutableSetOf()
                )
                state = state.copy(currentHand = Hand(handState))
            }
            is AutoMoveHandForwardEvent -> { }
            is PlayerCalledEvent -> state.currentHand!!.applyEvent(event)
            is PlayerCheckedEvent -> state.currentHand!!.applyEvent(event)
            is PlayerForceCheckedEvent -> state.currentHand!!.applyEvent(event)
            is PlayerFoldedEvent -> state.currentHand!!.applyEvent(event)
            is PlayerForceFoldedEvent -> state.currentHand!!.applyEvent(event)
            is PlayerRaisedEvent -> state.currentHand!!.applyEvent(event)
            is FlopCardsDealtEvent -> state.currentHand!!.applyEvent(event)
            is TurnCardDealtEvent -> state.currentHand!!.applyEvent(event)
            is RiverCardDealtEvent -> state.currentHand!!.applyEvent(event)
            is PotAmountIncreasedEvent -> state.currentHand!!.applyEvent(event)
            is PotClosedEvent -> state.currentHand!!.applyEvent(event)
            is PotCreatedEvent -> state.currentHand!!.applyEvent(event)
            is RoundCompletedEvent -> state.currentHand!!.applyEvent(event)
            is ActionOnChangedEvent -> state.currentHand!!.applyEvent(event)
            is LastToActChangedEvent -> state.currentHand!!.applyEvent(event)
            is WinnersDeterminedEvent -> state.currentHand!!.applyEvent(event)
            is HandCompletedEvent -> {
                state = state.copy(
                    currentHand = null,
                    chipsInBack = event.playerToChipsAtTableMap
                )
            }
            is TablePausedEvent -> {
                state = state.copy(paused = true)
            }
            is TableResumedEvent -> {
                state = state.copy(paused = false)
            }
            is PlayerAddedEvent -> {
                state = state.copy(
                    seatMap = state.seatMap.plus(event.position, event.playerId),
                    chipsInBack = state.chipsInBack.plus(event.playerId, event.chipsInBack)
                )
            }
            is PlayerRemovedEvent -> {
                val position = state.seatMap.entries.first { it.value == event.playerId }.key
                state = state.copy(
                    seatMap = state.seatMap.minus(position),
                    chipsInBack = state.chipsInBack.minus(event.playerId)
                )
            }
            is PlayerBustedTableEvent -> {
                val position = state.seatMap.entries.first { it.value == event.playerId }.key
                state = state.copy(
                    seatMap = state.seatMap.minus(position),
                    chipsInBack = state.chipsInBack.minus(event.playerId)
                )
            }
        }
    }

    private fun applyCommonEvent(event: TableEvent) {
        applyEvent(event)
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
        val playerToPocketCardsMap = HashMap<UUID?, PocketCards>()
        for (pocketCards in cardsUsedInHand.pocketCards) {
            val playerIdAtPosition = state.seatMap[nextToReceivePocketCards]
            playerToPocketCardsMap[playerIdAtPosition] = pocketCards
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards)
            handEvaluations[pocketCards]!!.playerId = playerIdAtPosition
        }
        val playersStillInHand = state.seatMap.values.filterNotNull().toSet()
        val possibleSeatActionsMap = HashMap<UUID?, Set<PlayerAction>>()
        playersStillInHand.forEach { possibleSeatActionsMap[it] = HashSet() }

        val handState = HandState(state.gameId, state.aggregateId, UUID.randomUUID(), state.seatMap,
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard,
            cardsUsedInHand.riverCard, state.buttonOnPosition, state.smallBlindPosition,
            state.bigBlindPosition, null,
            HashTreePMap.from(playerToPocketCardsMap),
            HashTreePMap.from(possibleSeatActionsMap),
            HashTreePSet.from(playersStillInHand), TreePVector.from(handEvaluations.values),
            HandDealerState.NONE, state.chipsInBack, HashTreePMap.empty(), HashTreePMap.empty(),
            HashTreePMap.empty(), smallBlind, bigBlind, 0, null,
            HashTreePSet.empty(), false, false, false, mutableSetOf())
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