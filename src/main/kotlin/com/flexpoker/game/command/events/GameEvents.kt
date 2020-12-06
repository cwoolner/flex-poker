package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import com.flexpoker.game.command.framework.GameEvent
import com.flexpoker.util.StringUtils
import java.time.Instant
import java.util.UUID

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

    override fun toString(): String {
        return StringUtils.allFieldsToString(this)
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
