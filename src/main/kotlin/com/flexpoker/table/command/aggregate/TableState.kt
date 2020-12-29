package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import org.pcollections.HashTreePMap
import org.pcollections.PMap
import org.pcollections.PSet
import org.pcollections.PVector
import java.util.UUID

data class TableState(
    val aggregateId: UUID,
    val gameId: UUID,
    val seatMap: PMap<Int, UUID?>,
    val startingNumberOfChips: Int,
    val chipsInBack: PMap<UUID, Int> = HashTreePMap.empty(),
    val buttonOnPosition: Int = 0,
    val smallBlindPosition: Int = 0,
    val bigBlindPosition: Int = 0,
    val currentHand: Hand? = null,
    val paused: Boolean = false
)

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