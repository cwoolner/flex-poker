package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.framework.event.Event
import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import java.time.Instant
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = BlindsIncreasedEvent::class, name = "BlindsIncreased"),
    JsonSubTypes.Type(value = GameCreatedEvent::class, name = "GameCreated"),
    JsonSubTypes.Type(value = GameFinishedEvent::class, name = "GameFinished"),
    JsonSubTypes.Type(value = GameJoinedEvent::class, name = "GameJoined"),
    JsonSubTypes.Type(value = GameMovedToStartingStageEvent::class, name = "GameMovedToStartingStage"),
    JsonSubTypes.Type(value = GameStartedEvent::class, name = "GameStarted"),
    JsonSubTypes.Type(value = GameTablesCreatedAndPlayersAssociatedEvent::class, name = "GameTablesCreatedAndPlayersAssociated"),
    JsonSubTypes.Type(value = NewHandIsClearedToStartEvent::class, name = "NewHandIsClearedToStart"),
    JsonSubTypes.Type(value = PlayerBustedGameEvent::class, name = "PlayerBustedGame"),
    JsonSubTypes.Type(value = PlayerMovedToNewTableEvent::class, name = "PlayerMovedToNewTable"),
    JsonSubTypes.Type(value = TablePausedForBalancingEvent::class, name = "TablePausedForBalancing"),
    JsonSubTypes.Type(value = TableRemovedEvent::class, name = "TableRemoved"),
    JsonSubTypes.Type(value = TableResumedAfterBalancingEvent::class, name = "TableResumedAfterBalancing")
)
interface GameEvent : Event

sealed class BaseGameEvent(private val aggregateId: UUID) : GameEvent {
    private var version = 0
    private val time: Instant = Instant.now()

    @JsonProperty("gameId")
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

}

data class BlindsIncreasedEvent (val gameId: UUID) : BaseGameEvent(gameId)

data class GameCreatedEvent (val gameId: UUID, val gameName: String, val numberOfPlayers: Int,
                             val numberOfPlayersPerTable: Int, val createdByPlayerId: UUID,
                             val numberOfMinutesBetweenBlindLevels: Int,
                             val numberOfSecondsForActionOnTimer: Int) : BaseGameEvent(gameId)

data class GameFinishedEvent (val gameId: UUID) : BaseGameEvent(gameId)

data class GameJoinedEvent (val gameId: UUID, val playerId: UUID) : BaseGameEvent(gameId)

data class GameMovedToStartingStageEvent (val gameId: UUID) : BaseGameEvent(gameId)

data class GameStartedEvent (val gameId: UUID, val tableIds: Set<UUID>,
                             val blindScheduleDTO: BlindScheduleDTO) : BaseGameEvent(gameId)

data class GameTablesCreatedAndPlayersAssociatedEvent (val gameId: UUID,
                                                       val tableIdToPlayerIdsMap: Map<UUID, Set<UUID>>,
                                                       val numberOfPlayersPerTable: Int) : BaseGameEvent(gameId)

data class NewHandIsClearedToStartEvent (val gameId: UUID, val tableId: UUID,
                                         val blindAmountsDTO: BlindAmountsDTO) : BaseGameEvent(gameId)

data class PlayerBustedGameEvent (val gameId: UUID, val playerId: UUID) : BaseGameEvent(gameId)

data class PlayerMovedToNewTableEvent (val gameId: UUID, val fromTableId: UUID, val toTableId: UUID,
                                       val playerId: UUID, val chips: Int) : BaseGameEvent(gameId)

data class TablePausedForBalancingEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)

data class TableRemovedEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)

data class TableResumedAfterBalancingEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)
