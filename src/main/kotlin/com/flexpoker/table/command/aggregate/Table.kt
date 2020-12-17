package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.framework.command.EventApplier
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
import org.pcollections.PMap
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.Random
import java.util.UUID
import java.util.function.Consumer

class Table(
    creatingFromEvents: Boolean, val aggregateId: UUID, private val gameId: UUID,
    private var seatMap: PMap<Int, UUID?>, startingNumberOfChips: Int
) {
    private val newEvents = ArrayList<TableEvent>()
    private val appliedEvents = ArrayList<TableEvent>()
    private val methodTable: MutableMap<Class<out TableEvent>, EventApplier<in TableEvent>> = HashMap()
    private val chipsInBack = HashMap<UUID, Int>()
    private var buttonOnPosition = 0
    private var smallBlindPosition = 0
    private var bigBlindPosition = 0
    private var currentHand: Hand? = null
    private var paused = false

    init {
        seatMap.values.filterNotNull().forEach { chipsInBack[it] = startingNumberOfChips }
        populateMethodTable()
        if (!creatingFromEvents) {
            val tableCreatedEvent = TableCreatedEvent(aggregateId, gameId, seatMap.size,
                HashTreePMap.from(seatMap), startingNumberOfChips)
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

    private fun populateMethodTable() {
        methodTable[TableCreatedEvent::class.java] = EventApplier { }
        methodTable[CardsShuffledEvent::class.java] = EventApplier { }
        methodTable[HandDealtEvent::class.java] =
            EventApplier { x: TableEvent -> applyHandDealtEvent(x as HandDealtEvent) }
        methodTable[AutoMoveHandForwardEvent::class.java] = EventApplier { }
        methodTable[PlayerCalledEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerCalledEvent))
        }
        methodTable[PlayerCheckedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerCheckedEvent))
        }
        methodTable[PlayerForceCheckedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerForceCheckedEvent))
        }
        methodTable[PlayerFoldedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerFoldedEvent))
        }
        methodTable[PlayerForceFoldedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerForceFoldedEvent))
        }
        methodTable[PlayerRaisedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PlayerRaisedEvent))
        }
        methodTable[FlopCardsDealtEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as FlopCardsDealtEvent))
        }
        methodTable[TurnCardDealtEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as TurnCardDealtEvent))
        }
        methodTable[RiverCardDealtEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as RiverCardDealtEvent))
        }
        methodTable[PotAmountIncreasedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PotAmountIncreasedEvent))
        }
        methodTable[PotClosedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PotClosedEvent))
        }
        methodTable[PotCreatedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as PotCreatedEvent))
        }
        methodTable[RoundCompletedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as RoundCompletedEvent))
        }
        methodTable[ActionOnChangedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as ActionOnChangedEvent))
        }
        methodTable[LastToActChangedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as LastToActChangedEvent))
        }
        methodTable[WinnersDeterminedEvent::class.java] = EventApplier { x: TableEvent ->
            currentHand!!.applyEvent((x as WinnersDeterminedEvent))
        }
        methodTable[HandCompletedEvent::class.java] = EventApplier { x: TableEvent ->
            val (_, _, _, playerToChipsAtTableMap) = x as HandCompletedEvent
            currentHand = null
            chipsInBack.clear()
            chipsInBack.putAll(HashMap(playerToChipsAtTableMap))
        }
        methodTable[TablePausedEvent::class.java] = EventApplier { paused = true }
        methodTable[TableResumedEvent::class.java] = EventApplier { paused = false }
        methodTable[PlayerAddedEvent::class.java] = EventApplier { x: TableEvent ->
            val event = x as PlayerAddedEvent
            seatMap = seatMap.plus(event.position, event.playerId)
            chipsInBack[event.playerId] = event.chipsInBack
        }
        methodTable[PlayerRemovedEvent::class.java] = EventApplier { x: TableEvent ->
            val event = x as PlayerRemovedEvent
            val position = seatMap.entries.first { it.value == event.playerId }.key
            seatMap = seatMap.minus(position)
            chipsInBack.remove(event.playerId)
        }
        methodTable[PlayerBustedTableEvent::class.java] = EventApplier { x: TableEvent ->
            val event = x as PlayerBustedTableEvent
            val position = seatMap.entries.first { it.value == event.playerId }.key
            seatMap = seatMap.minus(position)
            chipsInBack.remove(event.playerId)
        }
    }

    private fun applyCommonEvent(event: TableEvent) {
        methodTable[event.javaClass]!!.applyEvent(event)
        appliedEvents.add(event)
    }

    private fun applyHandDealtEvent(event: HandDealtEvent) {
        buttonOnPosition = event.buttonOnPosition
        smallBlindPosition = event.smallBlindPosition
        bigBlindPosition = event.bigBlindPosition
        currentHand = Hand(
            event.gameId, event.aggregateId,
            event.handId, seatMap, event.flopCards, event.turnCard,
            event.riverCard, event.buttonOnPosition,
            event.smallBlindPosition, event.bigBlindPosition,
            event.lastToActPlayerId, event.playerToPocketCardsMap,
            event.possibleSeatActionsMap, event.playersStillInHand,
            event.handEvaluations, event.handDealerState,
            event.chipsInBack, event.chipsInFrontMap,
            event.callAmountsMap, event.raiseToAmountsMap,
            event.smallBlind, event.bigBlind
        )
    }

    fun startNewHandForNewGame(
        smallBlind: Int, bigBlind: Int,
        shuffledDeckOfCards: List<Card>, cardsUsedInHand: CardsUsedInHand,
        handEvaluations: Map<PocketCards, HandEvaluation>
    ) {
        buttonOnPosition = assignButtonOnForNewGame()
        smallBlindPosition = assignSmallBlindForNewGame()
        bigBlindPosition = assignBigBlindForNewGame()
        val actionOnPosition = assignActionOnForNewHand()
        performNewHandCommonLogic(
            smallBlind, bigBlind, shuffledDeckOfCards,
            cardsUsedInHand, handEvaluations, actionOnPosition
        )
    }

    fun startNewHandForExistingTable(
        smallBlind: Int, bigBlind: Int,
        shuffledDeckOfCards: List<Card>, cardsUsedInHand: CardsUsedInHand,
        handEvaluations: Map<PocketCards, HandEvaluation>
    ) {
        buttonOnPosition = assignButtonOnForNewHand()
        smallBlindPosition = assignSmallBlindForNewHand()
        bigBlindPosition = assignBigBlindForNewHand()
        val actionOnPosition = assignActionOnForNewHand()
        performNewHandCommonLogic(
            smallBlind, bigBlind, shuffledDeckOfCards,
            cardsUsedInHand, handEvaluations, actionOnPosition
        )
    }

    private fun assignButtonOnForNewGame(): Int {
        while (true) {
            val potentialButtonOnPosition = Random().nextInt(seatMap.size)
            if (seatMap[Integer.valueOf(potentialButtonOnPosition)] != null) {
                return potentialButtonOnPosition
            }
        }
    }

    private fun assignSmallBlindForNewGame(): Int {
        return if (numberOfPlayersAtTable == 2) buttonOnPosition else findNextFilledSeat(buttonOnPosition)
    }

    private fun assignBigBlindForNewGame(): Int {
        return if (numberOfPlayersAtTable == 2) findNextFilledSeat(buttonOnPosition) else findNextFilledSeat(
            smallBlindPosition
        )
    }

    private fun assignActionOnForNewHand(): Int {
        return findNextFilledSeat(bigBlindPosition)
    }

    private fun assignBigBlindForNewHand(): Int {
        return findNextFilledSeat(bigBlindPosition)
    }

    private fun assignSmallBlindForNewHand(): Int {
        return findNextFilledSeat(smallBlindPosition)
    }

    private fun assignButtonOnForNewHand(): Int {
        return findNextFilledSeat(buttonOnPosition)
    }

    private fun findNextFilledSeat(startingPosition: Int): Int {
        for (i in startingPosition + 1 until seatMap.size) {
            if (seatMap[i] != null) {
                return i
            }
        }
        for (i in 0 until startingPosition) {
            if (seatMap[i] != null) {
                return i
            }
        }
        throw IllegalStateException("unable to find next filled seat")
    }

    private fun performNewHandCommonLogic(
        smallBlind: Int, bigBlind: Int,
        shuffledDeckOfCards: List<Card>, cardsUsedInHand: CardsUsedInHand,
        handEvaluations: Map<PocketCards, HandEvaluation>,
        actionOnPosition: Int
    ) {
        val cardsShuffledEvent = CardsShuffledEvent(aggregateId, gameId, shuffledDeckOfCards)
        newEvents.add(cardsShuffledEvent)
        applyCommonEvent(cardsShuffledEvent)
        var nextToReceivePocketCards = findNextFilledSeat(buttonOnPosition)
        val playerToPocketCardsMap = HashMap<UUID?, PocketCards>()
        for (pocketCards in cardsUsedInHand.pocketCards) {
            val playerIdAtPosition = seatMap[Integer
                .valueOf(nextToReceivePocketCards)]
            playerToPocketCardsMap[playerIdAtPosition] = pocketCards
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards)
            handEvaluations[pocketCards]!!.playerId = playerIdAtPosition
        }
        val playersStillInHand = seatMap.values.filterNotNull().toSet()
        val possibleSeatActionsMap = HashMap<UUID?, Set<PlayerAction>>()
        playersStillInHand.forEach { possibleSeatActionsMap[it] = HashSet() }
        val hand = Hand(
            gameId, aggregateId, UUID.randomUUID(), seatMap,
            cardsUsedInHand.flopCards, cardsUsedInHand.turnCard,
            cardsUsedInHand.riverCard, buttonOnPosition, smallBlindPosition,
            bigBlindPosition, null,
            HashTreePMap.from(playerToPocketCardsMap),
            HashTreePMap.from(possibleSeatActionsMap),
            playersStillInHand, ArrayList(handEvaluations.values),
            HandDealerState.NONE, chipsInBack, HashMap(), HashMap(),
            HashMap(), smallBlind, bigBlind
        )
        val eventsCreated = hand.dealHand(actionOnPosition)
        eventsCreated.forEach {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    val numberOfPlayersAtTable: Int
        get() = seatMap.values.filterNotNull().count()

    fun check(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerCheckedEvent = currentHand!!.check(playerId!!, false)
        newEvents.add(playerCheckedEvent)
        applyCommonEvent(playerCheckedEvent)
        handleEndOfRound()
    }

    fun call(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerCalledEvent = currentHand!!.call(playerId!!)
        newEvents.add(playerCalledEvent)
        applyCommonEvent(playerCalledEvent)
        handleEndOfRound()
    }

    fun fold(playerId: UUID?) {
        checkHandIsBeingPlayed()
        val playerFoldedEvent = currentHand!!.fold(playerId!!, false)
        newEvents.add(playerFoldedEvent)
        applyCommonEvent(playerFoldedEvent)
        handleEndOfRound()
    }

    fun raise(playerId: UUID?, raiseToAmount: Int) {
        checkHandIsBeingPlayed()
        val playerRaisedEvent = currentHand!!.raise(playerId!!, raiseToAmount)
        newEvents.add(playerRaisedEvent)
        applyCommonEvent(playerRaisedEvent)
        changeActionOnIfAppropriate()
    }

    fun expireActionOn(handId: UUID?, playerId: UUID?) {
        checkHandIsBeingPlayed()
        if (!currentHand!!.idMatches(handId!!)) {
            return
        }
        val forcedActionOnExpiredEvent = currentHand!!.expireActionOn(playerId!!)
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
        if (currentHand == null) {
            throw FlexPokerException("no hand in progress")
        }
    }

    private fun handlePotAndRoundCompleted() {
        val endOfRoundEvents = currentHand!!.handlePotAndRoundCompleted()
        endOfRoundEvents.forEach {
            newEvents.add(it)
            // TODO: not using applyCommonEvent() here because PotHandler is too
            // stateful and the events get applied to the state down there. when
            // that's refactored, this should change
            appliedEvents.add(it)
        }
    }

    private fun changeActionOnIfAppropriate() {
        val actionOnChangedEvents = currentHand!!.changeActionOn()
        actionOnChangedEvents.forEach {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun dealCommonCardsIfAppropriate() {
        currentHand!!.dealCommonCardsIfAppropriate().ifPresent {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun determineWinnersIfAppropriate() {
        currentHand!!.determineWinnersIfAppropriate().ifPresent {
            newEvents.add(it)
            applyCommonEvent(it)
        }
    }

    private fun removeAnyBustedPlayers() {
        chipsInBack.entries.filter { it.value == 0 }.forEach {
            val event = PlayerBustedTableEvent(aggregateId, gameId, it.key)
            newEvents.add(event)
            applyCommonEvent(event)
        }
    }

    private fun finishHandIfAppropriate() {
        val handCompleteEvent = currentHand!!.finishHandIfAppropriate()
        if (handCompleteEvent.isPresent) {
            newEvents.add(handCompleteEvent.get())
            applyCommonEvent(handCompleteEvent.get())
        } else {
            currentHand!!.autoMoveHandForward().ifPresent {
                newEvents.add(it)
                applyCommonEvent(it)
            }
        }
    }

    fun addPlayer(playerId: UUID?, chips: Int) {
        if (seatMap.values.contains(playerId)) {
            throw FlexPokerException("player already at this table")
        }
        val newPlayerPosition = findRandomOpenSeat()
        val playerAddedEvent = PlayerAddedEvent(aggregateId, gameId, playerId!!, chips, newPlayerPosition)
        newEvents.add(playerAddedEvent)
        applyCommonEvent(playerAddedEvent)
    }

    fun removePlayer(playerId: UUID?) {
        if (!seatMap.values.contains(playerId)) {
            throw FlexPokerException("player not at this table")
        }
        if (currentHand != null) {
            throw FlexPokerException("can't remove a player while in a hand")
        }
        val playerRemovedEvent = PlayerRemovedEvent(aggregateId, gameId, playerId!!)
        newEvents.add(playerRemovedEvent)
        applyCommonEvent(playerRemovedEvent)
    }

    fun pause() {
        if (paused) {
            throw FlexPokerException("table is already paused.  can't pause again.")
        }
        val tablePausedEvent = TablePausedEvent(aggregateId, gameId)
        newEvents.add(tablePausedEvent)
        applyCommonEvent(tablePausedEvent)
    }

    fun resume() {
        if (!paused) {
            throw FlexPokerException("table is not paused.  can't resume.")
        }
        val tableResumedEvent = TableResumedEvent(aggregateId, gameId)
        newEvents.add(tableResumedEvent)
        applyCommonEvent(tableResumedEvent)
    }

    private fun findRandomOpenSeat(): Int {
        while (true) {
            val potentialNewPlayerPosition = Random().nextInt(seatMap.size)
            if (seatMap[potentialNewPlayerPosition] == null) {
                return potentialNewPlayerPosition
            }
        }
    }

}