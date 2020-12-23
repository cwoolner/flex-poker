package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameFinishedEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.game.command.events.TableRemovedEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import com.flexpoker.game.query.dto.GameStage
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.PMap
import org.pcollections.PSet
import java.util.UUID

data class GameState(
    val aggregateId: UUID,
    val maxNumberOfPlayers: Int,
    val numberOfPlayersPerTable: Int,
    val stage: GameStage,
    val blindScheduleDTO: BlindScheduleDTO,
    val registeredPlayerIds: PSet<UUID> = HashTreePSet.empty(),
    val tableIdToPlayerIdsMap: PMap<UUID, PSet<UUID>> = HashTreePMap.empty(),
    val pausedTablesForBalancing: PSet<UUID> = HashTreePSet.empty()
)

fun applyEvents(events: List<GameEvent>): GameState {
    return applyEvents(null, *events.toTypedArray())
}

fun applyEvents(state: GameState?, vararg events: GameEvent): GameState {
    return events.fold(state, { acc, event -> applyEvent(acc, event) })!!
}

fun applyEvent(state: GameState?, event: GameEvent): GameState {
    require(state != null || event is GameCreatedEvent)
    return when(event) {
        is GameCreatedEvent -> GameState(
            event.aggregateId, event.numberOfPlayers, event.numberOfPlayersPerTable,
            GameStage.REGISTERING, blindSchedule(event.numberOfMinutesBetweenBlindLevels)
        )
        is GameJoinedEvent -> state!!.copy(registeredPlayerIds = state.registeredPlayerIds.plus(event.playerId))
        is GameMovedToStartingStageEvent -> state!!.copy(stage = GameStage.STARTING)
        is GameStartedEvent -> state!!.copy(stage = GameStage.INPROGRESS)
        is GameTablesCreatedAndPlayersAssociatedEvent ->
            state!!.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plusAll(event.tableIdToPlayerIdsMap))
        is GameFinishedEvent -> state!!.copy(stage = GameStage.FINISHED)
        is NewHandIsClearedToStartEvent -> state!!
        is BlindsIncreasedEvent -> incrementLevel(state!!)
        is TableRemovedEvent ->
            state!!.copy(
                tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.minus(event.tableId),
                pausedTablesForBalancing = state.pausedTablesForBalancing.minus(event.tableId)
            )
        is TablePausedForBalancingEvent ->
            state!!.copy(pausedTablesForBalancing = state.pausedTablesForBalancing.plus(event.tableId))
        is TableResumedAfterBalancingEvent ->
            state!!.copy(pausedTablesForBalancing = state.pausedTablesForBalancing.minus(event.tableId))
        is PlayerMovedToNewTableEvent -> {
            val fromPlayerIds = state!!.tableIdToPlayerIdsMap[event.fromTableId]!!.minus(event.playerId)
            val toPlayerIds = state.tableIdToPlayerIdsMap[event.fromTableId]!!.plus(event.playerId)
            state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap
                .plus(event.fromTableId, fromPlayerIds)
                .plus(event.toTableId, toPlayerIds))
        }
        is PlayerBustedGameEvent -> {
            val tableId = state!!.tableIdToPlayerIdsMap.entries.first { it.value.contains(event.playerId) }.key
            val playerIds = state.tableIdToPlayerIdsMap[tableId]!!.minus(event.playerId)
            state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plus(tableId, playerIds))
        }
        else -> throw IllegalArgumentException("invalid event to apply $event")
    }

}