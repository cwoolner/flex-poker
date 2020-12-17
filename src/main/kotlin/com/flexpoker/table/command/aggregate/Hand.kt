package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.pot.PotHandler
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
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import java.util.ArrayList
import java.util.HashSet
import java.util.Optional
import java.util.UUID

class Hand(
    private val gameId: UUID,
    private val tableId: UUID,
    private val entityId: UUID,
    private val seatMap: Map<Int, UUID?>,
    private val flopCards: FlopCards,
    private val turnCard: TurnCard,
    private val riverCard: RiverCard,
    private val buttonOnPosition: Int,
    private val smallBlindPosition: Int,
    private val bigBlindPosition: Int,
    private var lastToActPlayerId: UUID?,
    private val playerToPocketCardsMap: Map<UUID, PocketCards>,
    possibleSeatActionsMap: Map<UUID, Set<PlayerAction>>,
    playersStillInHand: Set<UUID>,
    private val handEvaluationList: List<HandEvaluation>,
    handDealerState: HandDealerState,
    chipsInBack: Map<UUID, Int>,
    chipsInFrontMap: Map<UUID, Int>,
    callAmountsMap: Map<UUID, Int>,
    raiseToAmountsMap: Map<UUID, Int>,
    smallBlind: Int,
    bigBlind: Int
) {
    private var actionOnPosition = 0
    private var possibleSeatActionsMap = HashTreePMap.from(possibleSeatActionsMap)
    private var originatingBettorPlayerId: UUID? = null
    private var handDealerState = handDealerState
    private var playersStillInHand = HashTreePSet.from(playersStillInHand)
    private var chipsInBackMap = HashTreePMap.from(chipsInBack)
    private var chipsInFrontMap = HashTreePMap.from(chipsInFrontMap)
    private var callAmountsMap = HashTreePMap.from(callAmountsMap)
    private var raiseToAmountsMap = HashTreePMap.from(raiseToAmountsMap)
    private val smallBlind: Int = smallBlind
    private val bigBlind: Int = bigBlind
    private val playersToShowCards = HashSet<UUID>()
    private var flopDealt = false
    private var turnDealt = false
    private var riverDealt = false
    private val potHandler = PotHandler(gameId, tableId, entityId, handEvaluationList)

    fun dealHand(actionOnPosition: Int): List<TableEvent> {
        val eventsCreated = ArrayList<TableEvent>()
        seatMap.keys.filter { seatMap[it] != null }.forEach { handleStartOfHandPlayerValues(it) }
        lastToActPlayerId = seatMap[Integer.valueOf(bigBlindPosition)]
        handDealerState = HandDealerState.POCKET_CARDS_DEALT
        val handDealtEvent = HandDealtEvent(
            tableId,
            gameId,
            entityId,
            flopCards,
            turnCard,
            riverCard,
            buttonOnPosition,
            smallBlindPosition,
            bigBlindPosition,
            lastToActPlayerId!!,
            HashTreePMap.from(seatMap),
            HashTreePMap.from(playerToPocketCardsMap),
            possibleSeatActionsMap,
            HashTreePSet.from(playersStillInHand),
            handEvaluationList,
            handDealerState,
            HashTreePMap.from(chipsInBackMap),
            HashTreePMap.from(chipsInFrontMap),
            HashTreePMap.from(callAmountsMap),
            HashTreePMap.from(raiseToAmountsMap),
            smallBlind,
            bigBlind
        )
        eventsCreated.add(handDealtEvent)

        // creat an initial empty pot for the table
        eventsCreated.add(PotCreatedEvent(tableId, gameId, entityId, UUID.randomUUID(), playersStillInHand))
        val actionOnPlayerId = seatMap[actionOnPosition]
        val actionOnChangedEvent = ActionOnChangedEvent(tableId, gameId, entityId, actionOnPlayerId!!)
        eventsCreated.add(actionOnChangedEvent)
        return eventsCreated
    }

    private fun handleStartOfHandPlayerValues(seatPosition: Int) {
        val playerId = seatMap[seatPosition]!!
        var chipsInFront = 0
        var callAmount = bigBlind
        val raiseToAmount = bigBlind * 2
        if (seatPosition == bigBlindPosition) {
            chipsInFront = bigBlind
            callAmount = 0
        } else if (seatPosition == smallBlindPosition) {
            chipsInFront = smallBlind
            callAmount = smallBlind
        }
        chipsInFrontMap = if (chipsInFront > chipsInBackMap[playerId]!!) {
            chipsInFrontMap.plus(playerId, chipsInBackMap[playerId])
        } else {
            chipsInFrontMap.plus(playerId, Integer.valueOf(chipsInFront))
        }
        subtractFromChipsInBack(playerId, chipsInFrontMap[playerId]!!)
        callAmountsMap = if (callAmount > chipsInBackMap[playerId]!!) {
            callAmountsMap.plus(playerId, chipsInBackMap[playerId])
        } else {
            callAmountsMap.plus(playerId, Integer.valueOf(callAmount))
        }
        val totalChips = chipsInBackMap[playerId]!! + chipsInFrontMap[playerId]!!
        raiseToAmountsMap = if (raiseToAmount > totalChips) {
            raiseToAmountsMap.plus(playerId, Integer.valueOf(totalChips))
        } else {
            raiseToAmountsMap.plus(playerId, Integer.valueOf(raiseToAmount))
        }
        if (raiseToAmountsMap[playerId]!! > 0) {
            possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId,
                HashTreePSet.singleton(PlayerAction.RAISE))
        }
        if (callAmountsMap[playerId]!! > 0) {
            val playerActions = possibleSeatActionsMap[playerId]!!.plus(setOf(PlayerAction.CALL, PlayerAction.FOLD))
            possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, playerActions)
        } else {
            val playerActions = possibleSeatActionsMap[playerId]!!.plus(PlayerAction.CHECK)
            possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, playerActions)
        }
    }

    fun check(playerId: UUID, forced: Boolean): TableEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.CHECK)
        return if (forced) PlayerForceCheckedEvent(tableId, gameId, entityId, playerId)
        else PlayerCheckedEvent(tableId, gameId, entityId, playerId)
    }

    fun call(playerId: UUID): PlayerCalledEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.CALL)
        return PlayerCalledEvent(tableId, gameId, entityId, playerId)
    }

    fun fold(playerId: UUID, forced: Boolean): TableEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.FOLD)
        return if (forced) PlayerForceFoldedEvent(tableId, gameId, entityId, playerId)
        else PlayerFoldedEvent(tableId, gameId, entityId, playerId)
    }

    fun raise(playerId: UUID, raiseToAmount: Int): PlayerRaisedEvent {
        checkActionOnPlayer(playerId)
        checkPerformAction(playerId, PlayerAction.RAISE)
        checkRaiseAmountValue(playerId, raiseToAmount)
        return PlayerRaisedEvent(tableId, gameId, entityId, playerId, raiseToAmount)
    }

    fun expireActionOn(playerId: UUID): TableEvent {
        return if (callAmountsMap[playerId]!! == 0) check(playerId, true) else fold(playerId, true)
    }

    fun changeActionOn(): List<TableEvent> {
        if (chipsInBackMap.values.all { it == 0 }) {
            return emptyList()
        }
        val eventsCreated = ArrayList<TableEvent>()

        // do not change action on if the hand is over. starting a new hand
        // should adjust that
        if (handDealerState === HandDealerState.COMPLETE) {
            return eventsCreated
        }
        val actionOnPlayerId = seatMap[Integer.valueOf(actionOnPosition)]

        // the player just bet, so a new last to act needs to be determined
        if (actionOnPlayerId == originatingBettorPlayerId) {
            val nextPlayerToAct = findNextToAct()
            val lastPlayerToAct = seatMap[Integer.valueOf(determineLastToAct())]
            val actionOnChangedEvent = ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct)
            val lastToActChangedEvent = LastToActChangedEvent(tableId, gameId, entityId, lastPlayerToAct!!)
            eventsCreated.add(actionOnChangedEvent)
            eventsCreated.add(lastToActChangedEvent)
        } else if (actionOnPlayerId == lastToActPlayerId) {
            val nextPlayerToAct = findActionOnPlayerIdForNewRound()
            val newRoundLastPlayerToAct = seatMap[Integer.valueOf(determineLastToAct())]
            val actionOnChangedEvent = ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct)
            val lastToActChangedEvent = LastToActChangedEvent(tableId, gameId, entityId, newRoundLastPlayerToAct!!)
            eventsCreated.add(actionOnChangedEvent)
            eventsCreated.add(lastToActChangedEvent)
        } else {
            val nextPlayerToAct = findNextToAct()
            val actionOnChangedEvent = ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct)
            eventsCreated.add(actionOnChangedEvent)
        }
        return eventsCreated
    }

    fun dealCommonCardsIfAppropriate(): Optional<TableEvent> {
        if (handDealerState === HandDealerState.FLOP_DEALT && !flopDealt) {
            return Optional.of(FlopCardsDealtEvent(tableId, gameId, entityId))
        }
        if (handDealerState === HandDealerState.TURN_DEALT && !turnDealt) {
            return Optional.of(TurnCardDealtEvent(tableId, gameId, entityId))
        }
        return if (handDealerState === HandDealerState.RIVER_DEALT && !riverDealt) {
            Optional.of(RiverCardDealtEvent(tableId, gameId, entityId))
        } else Optional.empty()
    }

    fun handlePotAndRoundCompleted(): List<TableEvent> {
        if (seatMap[Integer.valueOf(actionOnPosition)] != lastToActPlayerId
            && playersStillInHand.size > 1
        ) {
            return emptyList()
        }
        val tableEvents = ArrayList<TableEvent>()
        tableEvents.addAll(potHandler.calculatePots(chipsInFrontMap, chipsInBackMap))
        val nextHandDealerState =
            if (playersStillInHand.size == 1) HandDealerState.COMPLETE else HandDealerState.values()[handDealerState.ordinal + 1]
        val roundCompletedEvent = RoundCompletedEvent(tableId, gameId, entityId, nextHandDealerState)
        tableEvents.add(roundCompletedEvent)
        applyEvent(roundCompletedEvent)
        return tableEvents
    }

    fun finishHandIfAppropriate(): Optional<TableEvent> {
        return if (handDealerState === HandDealerState.COMPLETE) {
            Optional.of(HandCompletedEvent(tableId, gameId, entityId, chipsInBackMap))
        } else Optional.empty()
    }

    private fun checkRaiseAmountValue(playerId: UUID, raiseToAmount: Int) {
        val playersTotalChips = chipsInBackMap[playerId]!! + chipsInFrontMap[playerId]!!
        require(!(raiseToAmount < raiseToAmountsMap[playerId]!! || raiseToAmount > playersTotalChips)) { "Raise amount must be between the minimum and maximum values." }
    }

    private fun checkActionOnPlayer(playerId: UUID) {
        val actionOnPlayerId = seatMap[Integer.valueOf(actionOnPosition)]
        if (playerId != actionOnPlayerId) {
            throw FlexPokerException("action is not on the player attempting action")
        }
    }

    private fun checkPerformAction(playerId: UUID, playerAction: PlayerAction) {
        if (!possibleSeatActionsMap[playerId]!!.contains(playerAction)) {
            throw FlexPokerException("not allowed to $playerAction")
        }
    }

    fun applyEvent(event: PlayerCheckedEvent) {
        val playerId = event.playerId
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
    }

    fun applyEvent(event: PlayerForceCheckedEvent) {
        val playerId = event.playerId
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
    }

    fun applyEvent(event: PlayerCalledEvent) {
        val playerId = event.playerId
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        val newChipsInFront = chipsInFrontMap[playerId]!! + callAmountsMap[playerId]!!
        chipsInFrontMap = chipsInFrontMap.plus(playerId, newChipsInFront)
        val newChipsInBack = chipsInBackMap[playerId]!! - callAmountsMap[playerId]!!
        chipsInBackMap = chipsInBackMap.plus(playerId, newChipsInBack)
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
    }

    fun applyEvent(event: PlayerFoldedEvent) {
        val playerId = event.playerId
        playersStillInHand = playersStillInHand.minus(playerId)
        potHandler.removePlayerFromAllPots(playerId)
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
    }

    fun applyEvent(event: PlayerForceFoldedEvent) {
        val playerId = event.playerId
        playersStillInHand = playersStillInHand.minus(playerId)
        potHandler.removePlayerFromAllPots(playerId)
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
    }

    fun applyEvent(event: PlayerRaisedEvent) {
        val playerId = event.playerId
        val raiseToAmount = event.raiseToAmount
        originatingBettorPlayerId = playerId
        val raiseAboveCall = (raiseToAmount - (chipsInFrontMap[playerId]!! + callAmountsMap[playerId]!!))
        val increaseOfChipsInFront = raiseToAmount - chipsInFrontMap[playerId]!!
        playersStillInHand.filter { it != playerId }
            .forEach { adjustPlayersFieldsAfterRaise(raiseToAmount, raiseAboveCall, it) }
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId, HashTreePSet.empty())
        callAmountsMap = callAmountsMap.plus(playerId, 0)
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, bigBlind)
        chipsInFrontMap = chipsInFrontMap.plus(playerId, raiseToAmount)
        val newChipsInBack = chipsInBackMap[playerId]!! - increaseOfChipsInFront
        chipsInBackMap = chipsInBackMap.plus(playerId, newChipsInBack)
    }

    fun applyEvent(event: ActionOnChangedEvent) {
        actionOnPosition = seatMap.entries.first { it.value == event.playerId }.key
    }

    fun applyEvent(event: LastToActChangedEvent) {
        lastToActPlayerId = event.playerId
    }

    fun applyEvent(event: FlopCardsDealtEvent) {
        flopDealt = true
    }

    fun applyEvent(event: TurnCardDealtEvent) {
        turnDealt = true
    }

    fun applyEvent(event: RiverCardDealtEvent) {
        riverDealt = true
    }

    private fun findActionOnPlayerIdForNewRound(): UUID {
        val buttonIndex = buttonOnPosition
        for (i in buttonIndex + 1 until seatMap.size) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                && chipsInBackMap[playerAtTable] != 0
            ) {
                return playerAtTable
            }
        }
        for (i in 0 until buttonIndex) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                && chipsInBackMap[playerAtTable] != 0
            ) {
                return playerAtTable
            }
        }
        throw FlexPokerException("couldn't determine new action on after round")
    }

    private fun resetChipsInFront() {
        chipsInFrontMap.keys.forEach { chipsInFrontMap = chipsInFrontMap.plus(it, 0) }
    }

    private fun resetCallAndRaiseAmountsAfterRound() {
        playersStillInHand
            .forEach {
                callAmountsMap = callAmountsMap.plus(it, 0)
                raiseToAmountsMap = if (bigBlind > chipsInBackMap[it]!!) {
                    raiseToAmountsMap.plus(it, chipsInBackMap[it])
                } else {
                    raiseToAmountsMap.plus(it, bigBlind)
                }
            }
    }

    private fun resetPossibleSeatActionsAfterRound() {
        playersStillInHand.forEach {
            possibleSeatActionsMap = possibleSeatActionsMap.plus(it,
                HashTreePSet.from(setOf(PlayerAction.CHECK, PlayerAction.RAISE)))
        }
    }

    private fun findNextToAct(): UUID {
        for (i in actionOnPosition + 1 until seatMap.size) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable
            }
        }
        for (i in 0 until actionOnPosition) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable
            }
        }
        throw IllegalStateException("unable to find next to act")
    }

    fun determineWinnersIfAppropriate(): Optional<WinnersDeterminedEvent> {
        if (handDealerState === HandDealerState.COMPLETE) {
            val playersRequiredToShowCards = potHandler.fetchPlayersRequiredToShowCards(playersStillInHand)
            val playersToChipsWonMap = potHandler.fetchChipsWon(playersStillInHand)
            return Optional.of(WinnersDeterminedEvent(tableId, gameId, entityId,
                playersRequiredToShowCards, playersToChipsWonMap))
        }
        return Optional.empty()
    }

    private fun addToChipsInBack(playerId: UUID, chipsToAdd: Int) {
        val currentAmount = chipsInBackMap[playerId]!!
        chipsInBackMap = chipsInBackMap.plus(playerId, Integer.valueOf(currentAmount + chipsToAdd))
    }

    private fun subtractFromChipsInBack(playerId: UUID, chipsToSubtract: Int) {
        val currentAmount = chipsInBackMap[playerId]!!
        chipsInBackMap = chipsInBackMap.plus(playerId, currentAmount - chipsToSubtract)
    }

    private fun subtractFromChipsInFront(playerId: UUID, chipsToSubtract: Int) {
        val currentAmount = chipsInFrontMap[playerId]!!
        chipsInFrontMap = chipsInFrontMap.plus(playerId, currentAmount - chipsToSubtract)
    }

    private fun determineLastToAct(): Int {
        var seatIndex: Int
        if (originatingBettorPlayerId == null) {
            seatIndex = buttonOnPosition
        } else {
            seatIndex = seatMap.entries.first { it.value == originatingBettorPlayerId }.key
            if (seatIndex == 0) {
                seatIndex = seatMap.size - 1
            } else {
                seatIndex--
            }
        }
        for (i in seatIndex downTo 0) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i
            }
        }
        for (i in seatMap.size - 1 downTo seatIndex + 1) {
            val playerAtTable = seatMap[i]
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i
            }
        }
        throw IllegalStateException("unable to determine last to act")
    }

    fun idMatches(handId: UUID): Boolean {
        return entityId == handId
    }

    private fun adjustPlayersFieldsAfterRaise(raiseToAmount: Int, raiseAboveCall: Int, playerId: UUID) {
        possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId,
            HashTreePSet.from(setOf(PlayerAction.CALL, PlayerAction.FOLD)))
        val totalChips = chipsInBackMap[playerId]!! + chipsInFrontMap[playerId]!!
        if (totalChips <= raiseToAmount) {
            callAmountsMap = callAmountsMap.plus(playerId, totalChips - chipsInFrontMap[playerId]!!)
            raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0)
        } else {
            callAmountsMap = callAmountsMap.plus(playerId, raiseToAmount - chipsInFrontMap[playerId]!!)
            possibleSeatActionsMap = possibleSeatActionsMap.plus(playerId,
                HashTreePSet.from(setOf(PlayerAction.CALL, PlayerAction.FOLD, PlayerAction.RAISE)))
            raiseToAmountsMap = if (totalChips < raiseToAmount + raiseAboveCall) {
                raiseToAmountsMap.plus(playerId, totalChips)
            } else {
                raiseToAmountsMap.plus(playerId, raiseToAmount + raiseAboveCall)
            }
        }
    }

    fun applyEvent(event: PotAmountIncreasedEvent) {
        potHandler.addToPot(event.potId, event.amountIncreased)
        playersStillInHand.forEach { subtractFromChipsInFront(it, event.amountIncreased) }
    }

    fun applyEvent(event: PotClosedEvent) {
        potHandler.closePot(event.potId)
    }

    fun applyEvent(event: PotCreatedEvent) {
        potHandler.addNewPot(event.potId, event.playersInvolved)
    }

    fun applyEvent(event: RoundCompletedEvent) {
        handDealerState = event.nextHandDealerState
        originatingBettorPlayerId = null
        resetChipsInFront()
        resetCallAndRaiseAmountsAfterRound()
        resetPossibleSeatActionsAfterRound()
    }

    fun applyEvent(event: WinnersDeterminedEvent) {
        playersToShowCards.addAll(event.playersToShowCards)
        event.playersToChipsWonMap.forEach { (playerId: UUID, chips: Int) -> addToChipsInBack(playerId, chips) }
    }

    fun autoMoveHandForward(): Optional<TableEvent> {
        return if (chipsInBackMap.values.all { it == 0 }) Optional.of(AutoMoveHandForwardEvent(tableId, gameId, entityId))
        else Optional.empty()
    }

}