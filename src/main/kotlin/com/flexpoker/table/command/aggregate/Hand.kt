package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.pot.calculatePots
import com.flexpoker.table.command.aggregate.pot.fetchChipsWon
import com.flexpoker.table.command.aggregate.pot.fetchPlayersRequiredToShowCards
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import org.pcollections.HashTreePSet
import java.util.ArrayList
import java.util.Optional
import java.util.UUID

class Hand(var state: HandState) {

    fun dealHand(actionOnPosition: Int): List<TableEvent> {
        val eventsCreated = ArrayList<TableEvent>()
        state.seatMap.keys.filter { state.seatMap[it] != null }.forEach { handleStartOfHandPlayerValues(it) }
        state = state.copy(lastToActPlayerId = state.seatMap[state.bigBlindPosition])
        state = state.copy(handDealerState = HandDealerState.POCKET_CARDS_DEALT)
        val handDealtEvent = HandDealtEvent(
            state.tableId,
            state.gameId,
            state.entityId,
            state.flopCards,
            state.turnCard,
            state.riverCard,
            state.buttonOnPosition,
            state.smallBlindPosition,
            state.bigBlindPosition,
            state.lastToActPlayerId!!,
            state.seatMap,
            state.playerToPocketCardsMap,
            state.possibleSeatActionsMap,
            state.playersStillInHand,
            state.handEvaluationList,
            state.handDealerState,
            state.chipsInBackMap,
            state.chipsInFrontMap,
            state.callAmountsMap,
            state.raiseToAmountsMap,
            state.smallBlind,
            state.bigBlind
        )
        eventsCreated.add(handDealtEvent)

        // create an initial empty pot for the table
        eventsCreated.add(PotCreatedEvent(state.tableId, state.gameId, state.entityId, UUID.randomUUID(), state.playersStillInHand))
        val actionOnPlayerId = state.seatMap[actionOnPosition]
        val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, actionOnPlayerId!!)
        eventsCreated.add(actionOnChangedEvent)
        return eventsCreated
    }

    private fun handleStartOfHandPlayerValues(seatPosition: Int) {
        val playerId = state.seatMap[seatPosition]!!
        var chipsInFront = 0
        var callAmount = state.bigBlind
        val raiseToAmount = state.bigBlind * 2
        if (seatPosition == state.bigBlindPosition) {
            chipsInFront = state.bigBlind
            callAmount = 0
        } else if (seatPosition == state.smallBlindPosition) {
            chipsInFront = state.smallBlind
            callAmount = state.smallBlind
        }
        state = state.copy(
            chipsInFrontMap =
            if (chipsInFront > state.chipsInBackMap[playerId]!!)
                state.chipsInFrontMap.plus(playerId, state.chipsInBackMap[playerId])
            else
                state.chipsInFrontMap.plus(playerId, Integer.valueOf(chipsInFront))
        )
        subtractFromChipsInBack(playerId, state.chipsInFrontMap[playerId]!!)
        state = state.copy(
            callAmountsMap =
            if (callAmount > state.chipsInBackMap[playerId]!!)
                state.callAmountsMap.plus(playerId, state.chipsInBackMap[playerId])
            else
                state.callAmountsMap.plus(playerId, callAmount)
        )
        val totalChips = state.chipsInBackMap[playerId]!! + state.chipsInFrontMap[playerId]!!
        state = state.copy(
            raiseToAmountsMap =
            if (raiseToAmount > totalChips)
                state.raiseToAmountsMap.plus(playerId, totalChips)
            else
                state.raiseToAmountsMap.plus(playerId, raiseToAmount))

        if (state.raiseToAmountsMap[playerId]!! > 0) {
            state = state.copy(possibleSeatActionsMap = state.possibleSeatActionsMap.plus(playerId,
                HashTreePSet.singleton(PlayerAction.RAISE)))
        }
        state = if (state.callAmountsMap[playerId]!! > 0) {
            val playerActions = state.possibleSeatActionsMap[playerId]!!.plus(setOf(PlayerAction.CALL, PlayerAction.FOLD))
            state.copy(possibleSeatActionsMap = state.possibleSeatActionsMap.plus(playerId, playerActions))
        } else {
            val playerActions = state.possibleSeatActionsMap[playerId]!!.plus(PlayerAction.CHECK)
            state.copy(possibleSeatActionsMap = state.possibleSeatActionsMap.plus(playerId, playerActions))
        }
    }

    fun check(playerId: UUID, forced: Boolean): TableEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.CHECK)
        return if (forced) PlayerForceCheckedEvent(state.tableId, state.gameId, state.entityId, playerId)
        else PlayerCheckedEvent(state.tableId, state.gameId, state.entityId, playerId)
    }

    fun call(playerId: UUID): PlayerCalledEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.CALL)
        return PlayerCalledEvent(state.tableId, state.gameId, state.entityId, playerId)
    }

    fun fold(playerId: UUID, forced: Boolean): TableEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.FOLD)
        return if (forced) PlayerForceFoldedEvent(state.tableId, state.gameId, state.entityId, playerId)
        else PlayerFoldedEvent(state.tableId, state.gameId, state.entityId, playerId)
    }

    fun raise(playerId: UUID, raiseToAmount: Int): PlayerRaisedEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.RAISE)
        checkRaiseAmountValue(playerId, raiseToAmount)
        return PlayerRaisedEvent(state.tableId, state.gameId, state.entityId, playerId, raiseToAmount)
    }

    fun expireActionOn(playerId: UUID): TableEvent {
        return if (state.callAmountsMap[playerId]!! == 0) check(playerId, true) else fold(playerId, true)
    }

    fun changeActionOn(): List<TableEvent> {
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
                val nextPlayerToAct = findNextToAct()
                val lastPlayerToAct = state.seatMap[determineLastToAct()]
                val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
                val lastToActChangedEvent = LastToActChangedEvent(state.tableId, state.gameId, state.entityId, lastPlayerToAct!!)
                eventsCreated.add(actionOnChangedEvent)
                eventsCreated.add(lastToActChangedEvent)
            }
            state.lastToActPlayerId -> {
                val nextPlayerToAct = findActionOnPlayerIdForNewRound()
                val newRoundLastPlayerToAct = state.seatMap[Integer.valueOf(determineLastToAct())]
                val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
                val lastToActChangedEvent = LastToActChangedEvent(state.tableId, state.gameId, state.entityId, newRoundLastPlayerToAct!!)
                eventsCreated.add(actionOnChangedEvent)
                eventsCreated.add(lastToActChangedEvent)
            }
            else -> {
                val nextPlayerToAct = findNextToAct()
                val actionOnChangedEvent = ActionOnChangedEvent(state.tableId, state.gameId, state.entityId, nextPlayerToAct)
                eventsCreated.add(actionOnChangedEvent)
            }
        }
        return eventsCreated
    }

    fun dealCommonCardsIfAppropriate(): Optional<TableEvent> {
        if (state.handDealerState === HandDealerState.FLOP_DEALT && !state.flopDealt) {
            return Optional.of(FlopCardsDealtEvent(state.tableId, state.gameId, state.entityId))
        }
        if (state.handDealerState === HandDealerState.TURN_DEALT && !state.turnDealt) {
            return Optional.of(TurnCardDealtEvent(state.tableId, state.gameId, state.entityId))
        }
        return if (state.handDealerState === HandDealerState.RIVER_DEALT && !state.riverDealt) {
            Optional.of(RiverCardDealtEvent(state.tableId, state.gameId, state.entityId))
        } else Optional.empty()
    }

    fun handlePotAndRoundCompleted(): List<TableEvent> {
        if (state.seatMap[state.actionOnPosition] != state.lastToActPlayerId && state.playersStillInHand.size > 1) {
            return emptyList()
        }

        val (potEvents, updatedPots) = calculatePots(state.gameId, state.tableId, state.entityId, state.pots,
            state.handEvaluationList, state.chipsInFrontMap, state.chipsInBackMap)

        val tableEvents = ArrayList<TableEvent>()
        tableEvents.addAll(potEvents)
        state = state.copy(pots = updatedPots)
        val nextHandDealerState =
            if (state.playersStillInHand.size == 1) HandDealerState.COMPLETE
            else HandDealerState.values()[state.handDealerState.ordinal + 1]
        val roundCompletedEvent = RoundCompletedEvent(state.tableId, state.gameId, state.entityId, nextHandDealerState)
        tableEvents.add(roundCompletedEvent)
        state = applyEvent(state, roundCompletedEvent)
        return tableEvents
    }

    fun finishHandIfAppropriate(): Optional<TableEvent> {
        return if (state.handDealerState === HandDealerState.COMPLETE) {
            Optional.of(HandCompletedEvent(state.tableId, state.gameId, state.entityId, state.chipsInBackMap))
        } else Optional.empty()
    }

    private fun checkRaiseAmountValue(playerId: UUID, raiseToAmount: Int) {
        val playersTotalChips = state.chipsInBackMap[playerId]!! + state.chipsInFrontMap[playerId]!!
        require(!(raiseToAmount < state.raiseToAmountsMap[playerId]!! || raiseToAmount > playersTotalChips)) { "Raise amount must be between the minimum and maximum values." }
    }

    private fun checkActionOnPlayer(playerId: UUID) {
        val actionOnPlayerId = state.seatMap[state.actionOnPosition]
        if (playerId != actionOnPlayerId) {
            throw FlexPokerException("action is not on the player attempting action")
        }
    }

    private fun checkPerformAction(playerId: UUID, playerAction: PlayerAction) {
        if (!state.possibleSeatActionsMap[playerId]!!.contains(playerAction)) {
            throw FlexPokerException("not allowed to $playerAction")
        }
    }

    private fun findActionOnPlayerIdForNewRound(): UUID {
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

    private fun findNextToAct(): UUID {
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

    fun determineWinnersIfAppropriate(): Optional<WinnersDeterminedEvent> {
        if (state.handDealerState === HandDealerState.COMPLETE) {
            val playersRequiredToShowCards = fetchPlayersRequiredToShowCards(state.pots, state.playersStillInHand)
            val playersToChipsWonMap = fetchChipsWon(state.pots, state.playersStillInHand)
            return Optional.of(WinnersDeterminedEvent(state.tableId, state.gameId, state.entityId,
                playersRequiredToShowCards, playersToChipsWonMap))
        }
        return Optional.empty()
    }

    private fun subtractFromChipsInBack(playerId: UUID, chipsToSubtract: Int) {
        val currentAmount = state.chipsInBackMap[playerId]!!
        state = state.copy(chipsInBackMap = state.chipsInBackMap.plus(playerId, currentAmount - chipsToSubtract))
    }

    private fun determineLastToAct(): Int {
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

    fun idMatches(handId: UUID): Boolean {
        return state.entityId == handId
    }

    fun autoMoveHandForward(): Optional<TableEvent> {
        return if (state.chipsInBackMap.values.all { it == 0 })
            Optional.of(AutoMoveHandForwardEvent(state.tableId, state.gameId, state.entityId))
        else
            Optional.empty()
    }

}