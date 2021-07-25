package com.flexpoker.table.command.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.flexpoker.framework.event.Event
import com.flexpoker.table.command.Card
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.pcollections.PMap
import org.pcollections.PSet
import org.pcollections.PVector
import java.time.Instant
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ActionOnChangedEvent::class, name = "ActionOnChanged"),
    JsonSubTypes.Type(value = AutoMoveHandForwardEvent::class, name = "AutoMoveHandForward"),
    JsonSubTypes.Type(value = CardsShuffledEvent::class, name = "CardsShuffled"),
    JsonSubTypes.Type(value = FlopCardsDealtEvent::class, name = "FlopCardsDealt"),
    JsonSubTypes.Type(value = HandCompletedEvent::class, name = "HandCompleted"),
    JsonSubTypes.Type(value = HandDealtEvent::class, name = "HandDealt"),
    JsonSubTypes.Type(value = LastToActChangedEvent::class, name = "LastToActChanged"),
    JsonSubTypes.Type(value = PlayerAddedEvent::class, name = "PlayerAdded"),
    JsonSubTypes.Type(value = PlayerBustedTableEvent::class, name = "PlayerBustedTableEvent"),
    JsonSubTypes.Type(value = PlayerCalledEvent::class, name = "PlayerCalled"),
    JsonSubTypes.Type(value = PlayerCheckedEvent::class, name = "PlayerChecked"),
    JsonSubTypes.Type(value = PlayerFoldedEvent::class, name = "PlayerFolded"),
    JsonSubTypes.Type(value = PlayerForceCheckedEvent::class, name = "PlayerForceChecked"),
    JsonSubTypes.Type(value = PlayerForceFoldedEvent::class, name = "PlayerForceFolded"),
    JsonSubTypes.Type(value = PlayerRaisedEvent::class, name = "PlayerRaised"),
    JsonSubTypes.Type(value = PlayerRemovedEvent::class, name = "PlayerRemoved"),
    JsonSubTypes.Type(value = PotAmountIncreasedEvent::class, name = "PotAmountIncreased"),
    JsonSubTypes.Type(value = PotClosedEvent::class, name = "PotClosed"),
    JsonSubTypes.Type(value = PotCreatedEvent::class, name = "PotCreated"),
    JsonSubTypes.Type(value = RiverCardDealtEvent::class, name = "RiverCardDealt"),
    JsonSubTypes.Type(value = RoundCompletedEvent::class, name = "RoundCompleted"),
    JsonSubTypes.Type(value = TableCreatedEvent::class, name = "TableCreated"),
    JsonSubTypes.Type(value = TablePausedEvent::class, name = "TablePaused"),
    JsonSubTypes.Type(value = TableResumedEvent::class, name = "TableResumed"),
    JsonSubTypes.Type(value = TurnCardDealtEvent::class, name = "TurnCardDealt"),
    JsonSubTypes.Type(value = WinnersDeterminedEvent::class, name = "WinnersDetermined")
)
sealed interface TableEvent : Event

sealed class BaseTableEvent(override val aggregateId: UUID) : TableEvent {
    override var version = 0
    override val time: Instant = Instant.now()
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
    val seatMap: PMap<Int, UUID?>,
    val playerToPocketCardsMap: PMap<UUID, PocketCards>,
    val possibleSeatActionsMap: PMap<UUID, Set<PlayerAction>>,
    val playersStillInHand: PSet<UUID>,
    val handEvaluations: PVector<HandEvaluation>,
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
                              val seatPositionToPlayerMap: PMap<Int, UUID?>,
                              val startingNumberOfChips: Int) : BaseTableEvent(tableId)

data class TablePausedEvent (val tableId: UUID, val gameId: UUID) : BaseTableEvent(tableId)

data class TableResumedEvent (val tableId: UUID, val gameId: UUID) : BaseTableEvent(tableId)

data class TurnCardDealtEvent (val tableId: UUID, val gameId: UUID, val handId: UUID) : BaseTableEvent(tableId)

data class WinnersDeterminedEvent (val tableId: UUID, val gameId: UUID, val handId: UUID,
                                   val playersToShowCards: Set<UUID>,
                                   val playersToChipsWonMap: PMap<UUID, Int>) : BaseTableEvent(tableId)
