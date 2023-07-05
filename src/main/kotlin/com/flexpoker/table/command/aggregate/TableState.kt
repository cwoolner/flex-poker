package com.flexpoker.table.command.aggregate

import com.flexpoker.framework.command.DomainState
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
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
import com.flexpoker.util.toPMap
import org.pcollections.HashTreePSet
import org.pcollections.PMap
import org.pcollections.PSet
import org.pcollections.PVector
import java.util.UUID

data class TableState(
    val aggregateId: UUID,
    val gameId: UUID,
    val seatMap: PMap<Int, UUID?>,
    val startingNumberOfChips: Int,
    val chipsInBack: PMap<UUID, Int>,
    val buttonOnPosition: Int = 0,
    val smallBlindPosition: Int = 0,
    val bigBlindPosition: Int = 0,
    val currentHand: HandState? = null,
    val paused: Boolean = false
) : DomainState

data class HandState(
    val gameId: UUID,
    val tableId: UUID,
    val entityId: UUID,
    val seatMap: PMap<Int, UUID?>,
    val flopCards: FlopCards,
    val turnCard: TurnCard,
    val riverCard: RiverCard,
    val buttonOnPosition: Int,
    val smallBlindPosition: Int,
    val bigBlindPosition: Int,
    val lastToActPlayerId: UUID?,
    val playerToPocketCardsMap: PMap<UUID, PocketCards>,
    val possibleSeatActionsMap: PMap<UUID, Set<PlayerAction>>,
    val playersStillInHand: PSet<UUID>,
    val handEvaluationList: PVector<HandEvaluation>,
    val handDealerState: HandDealerState,
    val chipsInBackMap: PMap<UUID, Int>,
    val chipsInFrontMap: PMap<UUID, Int>,
    val callAmountsMap: PMap<UUID, Int>,
    val raiseToAmountsMap: PMap<UUID, Int>,
    val smallBlind: Int,
    val bigBlind: Int,
    val actionOnPosition: Int,
    val originatingBettorPlayerId: UUID?,
    val playersToShowCards: PSet<UUID>,
    val flopDealt: Boolean,
    val turnDealt: Boolean,
    val riverDealt: Boolean,
    val pots: PSet<PotState>
)

data class PotState(
    val id: UUID,
    val amount: Int,
    val isOpen: Boolean,
    val handEvaluations: PSet<HandEvaluation>
)

fun applyEvents(events: List<TableEvent>): TableState {
    return applyEvents(null, events)
}

fun applyEvents(state: TableState?, events: List<TableEvent>): TableState {
    return events.fold(state, { acc, event -> applyEvent(acc, event) })!!
}

fun applyEvent(state: TableState?, event: TableEvent): TableState {
    require(state != null || event is TableCreatedEvent)
    return when (event) {
        is TableCreatedEvent -> {
            val chipsInBack = event.seatPositionToPlayerMap.values
                .filterNotNull()
                .associateWith { event.startingNumberOfChips }.toPMap()
            TableState(event.aggregateId, event.gameId, event.seatPositionToPlayerMap,
                event.startingNumberOfChips, chipsInBack)
        }
        is CardsShuffledEvent -> state!!
        is HandDealtEvent ->
            state!!.copy(
                buttonOnPosition = event.buttonOnPosition,
                smallBlindPosition = event.smallBlindPosition,
                bigBlindPosition = event.bigBlindPosition,
                currentHand = HandState(event.gameId, event.aggregateId, event.handId, state.seatMap,
                    event.flopCards, event.turnCard, event.riverCard, event.buttonOnPosition, event.smallBlindPosition,
                    event.bigBlindPosition, event.lastToActPlayerId, event.playerToPocketCardsMap,
                    event.possibleSeatActionsMap, event.playersStillInHand, event.handEvaluations,
                    event.handDealerState, event.chipsInBack, event.chipsInFrontMap, event.callAmountsMap,
                    event.raiseToAmountsMap, event.smallBlind, event.bigBlind, 0, null,
                    HashTreePSet.empty(), false, false, false, HashTreePSet.empty()))
        is AutoMoveHandForwardEvent -> state!!
        is PlayerCalledEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PlayerCheckedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PlayerForceCheckedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PlayerFoldedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PlayerForceFoldedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PlayerRaisedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is FlopCardsDealtEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is TurnCardDealtEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is RiverCardDealtEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PotAmountIncreasedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PotClosedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is PotCreatedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is RoundCompletedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is ActionOnChangedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is LastToActChangedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is WinnersDeterminedEvent -> state!!.copy(currentHand = applyEvent(state.currentHand!!, event))
        is HandCompletedEvent -> state!!.copy(currentHand = null, chipsInBack = event.playerToChipsAtTableMap)
        is TablePausedEvent -> state!!.copy(paused = true)
        is TableResumedEvent -> state!!.copy(paused = false)
        is PlayerAddedEvent ->
            state!!.copy(
                seatMap = state.seatMap.plus(event.position, event.playerId),
                chipsInBack = state.chipsInBack.plus(event.playerId, event.chipsInBack)
            )
        is PlayerRemovedEvent -> {
            val position = state!!.seatMap.entries.first { it.value == event.playerId }.key
            state.copy(
                seatMap = state.seatMap.minus(position),
                chipsInBack = state.chipsInBack.minus(event.playerId)
            )
        }
        is PlayerBustedTableEvent -> {
            val position = state!!.seatMap.entries.first { it.value == event.playerId }.key
            state.copy(
                seatMap = state.seatMap.minus(position),
                chipsInBack = state.chipsInBack.minus(event.playerId)
            )
        }
    }
}

fun applyEvent(state: HandState, event: TableEvent): HandState {
    return when (event) {
        is PlayerCheckedEvent ->
            state.copy(
                possibleSeatActionsMap = state.possibleSeatActionsMap.plus(event.playerId, HashTreePSet.empty()),
                callAmountsMap = state.callAmountsMap.plus(event.playerId, 0),
                raiseToAmountsMap = state.raiseToAmountsMap.plus(event.playerId, 0)
            )
        is PlayerForceCheckedEvent ->
            state.copy(
                possibleSeatActionsMap = state.possibleSeatActionsMap.plus(event.playerId, HashTreePSet.empty()),
                callAmountsMap = state.callAmountsMap.plus(event.playerId, 0),
                raiseToAmountsMap = state.raiseToAmountsMap.plus(event.playerId, 0)
            )
        is PlayerCalledEvent -> {
            val playerId = event.playerId
            val newChipsInFront = state.chipsInFrontMap[playerId]!! + state.callAmountsMap[playerId]!!
            val newChipsInBack = state.chipsInBackMap[playerId]!! - state.callAmountsMap[playerId]!!
            state.copy(
                possibleSeatActionsMap = state.possibleSeatActionsMap.plus(playerId, HashTreePSet.empty()),
                chipsInFrontMap = state.chipsInFrontMap.plus(playerId, newChipsInFront),
                chipsInBackMap = state.chipsInBackMap.plus(playerId, newChipsInBack),
                callAmountsMap = state.callAmountsMap.plus(playerId, 0),
                raiseToAmountsMap = state.raiseToAmountsMap.plus(playerId, 0)
            )
        }
        is PlayerFoldedEvent ->
            state.copy(
                playersStillInHand = state.playersStillInHand.minus(event.playerId),
                possibleSeatActionsMap = state.possibleSeatActionsMap.plus(event.playerId, HashTreePSet.empty()),
                pots = removePlayerFromAllPots(state.pots, event.playerId),
                callAmountsMap = state.callAmountsMap.plus(event.playerId, 0),
                raiseToAmountsMap = state.raiseToAmountsMap.plus(event.playerId, 0)
            )
        is PlayerForceFoldedEvent ->
            state.copy(
                playersStillInHand = state.playersStillInHand.minus(event.playerId),
                possibleSeatActionsMap = state.possibleSeatActionsMap.plus(event.playerId, HashTreePSet.empty()),
                pots = removePlayerFromAllPots(state.pots, event.playerId),
                callAmountsMap = state.callAmountsMap.plus(event.playerId, 0),
                raiseToAmountsMap = state.raiseToAmountsMap.plus(event.playerId, 0)
            )
        is PlayerRaisedEvent -> {
            val playerId = event.playerId
            val raiseToAmount = event.raiseToAmount
            var updatedState = state.copy(originatingBettorPlayerId = playerId)
            val raiseAboveCall = (raiseToAmount - (updatedState.chipsInFrontMap[playerId]!! + updatedState.callAmountsMap[playerId]!!))
            val increaseOfChipsInFront = raiseToAmount - updatedState.chipsInFrontMap[playerId]!!
            updatedState.playersStillInHand
                .filter { it != playerId }
                .forEach { updatedState = adjustPlayersFieldsAfterRaise(updatedState, raiseToAmount, raiseAboveCall, it) }
            val newChipsInBack = updatedState.chipsInBackMap[playerId]!! - increaseOfChipsInFront

            updatedState.copy(
                possibleSeatActionsMap = updatedState.possibleSeatActionsMap.plus(playerId, HashTreePSet.empty()),
                callAmountsMap = updatedState.callAmountsMap.plus(playerId, 0),
                raiseToAmountsMap = updatedState.raiseToAmountsMap.plus(playerId, updatedState.bigBlind),
                chipsInFrontMap = updatedState.chipsInFrontMap.plus(playerId, raiseToAmount),
                chipsInBackMap = updatedState.chipsInBackMap.plus(playerId, newChipsInBack)
            )
        }
        is ActionOnChangedEvent -> state.copy(actionOnPosition = state.seatMap.entries.first { it.value == event.playerId }.key)
        is LastToActChangedEvent -> state.copy(lastToActPlayerId = event.playerId)
        is FlopCardsDealtEvent -> state.copy(flopDealt = true)
        is TurnCardDealtEvent -> state.copy(turnDealt = true)
        is RiverCardDealtEvent -> state.copy(riverDealt = true)
        is PotAmountIncreasedEvent -> {
            var updatedState = state.copy(pots = addToPot(state.pots, event.potId, event.amountIncreased))
            updatedState.playersStillInHand.forEach { updatedState = subtractFromChipsInFront(updatedState, it, event.amountIncreased) }
            updatedState
        }
        is PotClosedEvent -> state.copy(pots = closePot(state.pots, event.potId))
        is PotCreatedEvent -> state.copy(pots = addNewPot(state.pots, state.handEvaluationList, event.potId, event.playersInvolved))
        is RoundCompletedEvent -> {
            var updatedState = state.copy(
                handDealerState = event.nextHandDealerState,
                originatingBettorPlayerId = null
            )
            updatedState = resetChipsInFront(updatedState)
            updatedState = resetCallAndRaiseAmountsAfterRound(updatedState)
            updatedState = resetPossibleSeatActionsAfterRound(updatedState)
            updatedState
        }
        is WinnersDeterminedEvent -> {
            var updatedState = state.copy(playersToShowCards = state.playersToShowCards.plusAll(event.playersToShowCards))
            event.playersToChipsWonMap.forEach { updatedState = addToChipsInBack(updatedState, it.key, it.value) }
            updatedState
        }
        else -> throw IllegalArgumentException("invalid event to apply $event")
    }
}

private fun adjustPlayersFieldsAfterRaise(state: HandState, raiseToAmount: Int, raiseAboveCall: Int, playerId: UUID): HandState {
    var updatedState = state.copy(possibleSeatActionsMap = state.possibleSeatActionsMap
        .plus(playerId, setOf(PlayerAction.CALL, PlayerAction.FOLD)))
    val totalChips = updatedState.chipsInBackMap[playerId]!! + updatedState.chipsInFrontMap[playerId]!!
    if (totalChips <= raiseToAmount) {
        updatedState = updatedState.copy(callAmountsMap = updatedState.callAmountsMap.plus(playerId, totalChips - updatedState.chipsInFrontMap[playerId]!!))
        updatedState = updatedState.copy(raiseToAmountsMap = updatedState.raiseToAmountsMap.plus(playerId, 0))
    } else {
        updatedState = updatedState.copy(callAmountsMap = updatedState.callAmountsMap.plus(playerId, raiseToAmount - updatedState.chipsInFrontMap[playerId]!!))
        updatedState = updatedState.copy(possibleSeatActionsMap = updatedState.possibleSeatActionsMap.plus(playerId,
            setOf(PlayerAction.CALL, PlayerAction.FOLD, PlayerAction.RAISE)))
        updatedState = updatedState.copy(
            raiseToAmountsMap =
            if (totalChips < raiseToAmount + raiseAboveCall)
                updatedState.raiseToAmountsMap.plus(playerId, totalChips)
            else
                updatedState.raiseToAmountsMap.plus(playerId, raiseToAmount + raiseAboveCall))
    }
    return updatedState
}

private fun subtractFromChipsInFront(state: HandState, playerId: UUID, chipsToSubtract: Int): HandState {
    val currentAmount = state.chipsInFrontMap[playerId]!!
    return state.copy(chipsInFrontMap = state.chipsInFrontMap.plus(playerId, currentAmount - chipsToSubtract))
}

private fun resetChipsInFront(state: HandState): HandState {
    return state.copy(chipsInFrontMap = state.chipsInFrontMap.mapValues { 0 }.toPMap())
}

private fun resetCallAndRaiseAmountsAfterRound(state: HandState): HandState {
    return state.copy(
        callAmountsMap = state.callAmountsMap.mapValues { 0 }.toPMap(),
        raiseToAmountsMap = state.playersStillInHand.associateWith {
            if (state.bigBlind > state.chipsInBackMap[it]!!) state.chipsInBackMap[it]!! else state.bigBlind
        }.toPMap()
    )
}

private fun resetPossibleSeatActionsAfterRound(state: HandState): HandState {
    var updatedState = state
    updatedState.playersStillInHand.forEach {
        updatedState = updatedState.copy(possibleSeatActionsMap = updatedState.possibleSeatActionsMap
            .plus(it, setOf(PlayerAction.CHECK, PlayerAction.RAISE)))
    }
    return updatedState
}

private fun addToChipsInBack(state: HandState, playerId: UUID, chipsToAdd: Int): HandState {
    val currentAmount = state.chipsInBackMap[playerId]!!
    return state.copy(chipsInBackMap = state.chipsInBackMap.plus(playerId, currentAmount + chipsToAdd))
}
