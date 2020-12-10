package com.flexpoker.table.command.events

import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.Card
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.HandDealerState
import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.framework.TableEvent
import com.flexpoker.util.StringUtils
import org.pcollections.PMap
import org.pcollections.PSet
import java.time.Instant
import java.util.UUID

sealed class BaseTableEvent(private val aggregateId: UUID) : TableEvent {
    private var version = 0
    private val time: Instant = Instant.now()

    @JsonProperty
    override fun getAggregateId(): UUID {
        return aggregateId
    }

    @JsonProperty
    override fun getVersion(): Int {
        if (version == 0) {
            throw FlexPokerException("should be calling getVersion() in situations where it's already been set")
        }
        return version
    }

    override fun setVersion(version: Int) {
        this.version = version
    }

    @JsonProperty
    override fun getTime(): Instant {
        return time
    }

    override fun toString(): String {
        return StringUtils.allFieldsToString(this)
    }

}

data class ActionOnChangedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID, val playerId: UUID)
    : BaseTableEvent(tableId)

data class AutoMoveHandForwardEvent (val tableId: UUID, val gameId: UUID, val handId: UUID)
    : BaseTableEvent(tableId)

data class CardsShuffledEvent (val tableId: UUID, val gameId: UUID, val shuffledDeck: List<Card>)
    : BaseTableEvent(tableId)

data class FlopCardsDealtEvent (val tableId: UUID, val gameId: UUID, val handId: UUID) : BaseTableEvent(tableId)

data class HandCompletedEvent(val tableId: UUID, val gameId: UUID, val handId: UUID,
                              val playerToChipsAtTableMap: PMap<UUID, Int>) : BaseTableEvent(tableId)

data class HandDealtEvent (
    val tableId: UUID,
    val gameId: UUID,
    val handId: UUID,
    val flopCards: FlopCards,
    val turnCard: TurnCard,
    val riverCard: RiverCard,
    val buttonOnPosition: Int,
    val smallBlindPosition: Int,
    val bigBlindPosition: Int,
    val lastToActPlayerId: UUID,
    val seatMap: PMap<Int, UUID>,
    val playerToPocketCardsMap: PMap<UUID, PocketCards>,
    val possibleSeatActionsMap: PMap<UUID, Set<PlayerAction>>,
    val playersStillInHand: PSet<UUID>,
    val handEvaluations: List<HandEvaluation>,
    val handDealerState: HandDealerState,
    val chipsInBack: PMap<UUID, Int>,
    val chipsInFrontMap: PMap<UUID, Int>,
    val callAmountsMap: PMap<UUID, Int>,
    val raiseToAmountsMap: PMap<UUID, Int>,
    val smallBlind: Int,
    val bigBlind: Int) : BaseTableEvent(tableId)

data class LastToActChangedEvent (val tableId: UUID, val gameId: UUID,
                                  val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerAddedEvent (val tableId: UUID, val gameId: UUID, val playerId: UUID,
                             val chipsInBack: Int, val position: Int) : BaseTableEvent(tableId)

data class PlayerBustedTableEvent (val tableId: UUID, val gameId: UUID,
                                   val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerCalledEvent (val tableId: UUID, val gameId: UUID,
                              val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerCheckedEvent (val tableId: UUID, val gameId: UUID,
                               val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerFoldedEvent (val tableId: UUID, val gameId: UUID,
                              val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerForceCheckedEvent (val tableId: UUID, val gameId: UUID,
                                    val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerForceFoldedEvent (val tableId: UUID, val gameId: UUID,
                                   val handId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PlayerRaisedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID,
                              val playerId: UUID, val raiseToAmount: Int) : BaseTableEvent(tableId)

data class PlayerRemovedEvent (val tableId: UUID, val gameId: UUID, val playerId: UUID) : BaseTableEvent(tableId)

data class PotAmountIncreasedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID, val potId: UUID,
                                    val amountIncreased: Int) : BaseTableEvent(tableId)

data class PotClosedEvent (val tableId: UUID, val gameId: UUID,
                           val handId: UUID, val potId: UUID) : BaseTableEvent(tableId)

data class PotCreatedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID,
                            val potId: UUID, val playersInvolved: Set<UUID>) : BaseTableEvent(tableId)

data class RiverCardDealtEvent (val tableId: UUID, val gameId: UUID, val handId: UUID) : BaseTableEvent(tableId)

data class RoundCompletedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID,
                                val nextHandDealerState: HandDealerState) : BaseTableEvent(tableId)

data class TableCreatedEvent (val tableId: UUID, val gameId: UUID, val numberOfPlayersPerTable: Int,
                              val seatPositionToPlayerMap: PMap<Int, UUID>,
                              val startingNumberOfChips: Int) : BaseTableEvent(tableId)

data class TablePausedEvent (val tableId: UUID, val gameId: UUID) : BaseTableEvent(tableId)

data class TableResumedEvent (val tableId: UUID, val gameId: UUID) : BaseTableEvent(tableId)

data class TurnCardDealtEvent (val tableId: UUID, val gameId: UUID, val handId: UUID) : BaseTableEvent(tableId)

data class WinnersDeterminedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID,
                                   val playersToShowCards: Set<UUID>,
                                   val playersToChipsWonMap: PMap<UUID, Int>) : BaseTableEvent(tableId)
