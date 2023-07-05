package com.flexpoker.game.command.aggregate

import com.flexpoker.framework.command.DomainState
import com.flexpoker.game.command.GameStage
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
    val blindSchedule: BlindSchedule,
    val registeredPlayerIds: PSet<UUID> = HashTreePSet.empty(),
    val tableIdToPlayerIdsMap: PMap<UUID, PSet<UUID>> = HashTreePMap.empty(),
    val pausedTablesForBalancing: PSet<UUID> = HashTreePSet.empty()
) : DomainState

private val levelToBlindsMap = mapOf(
    1 to validateBlinds(10, 20),
    2 to validateBlinds(20, 40),
    3 to validateBlinds(40, 80),
    4 to validateBlinds(80, 160),
    5 to validateBlinds(160, 320)
)

private fun validateBlinds(smallBlind: Int, bigBlind: Int): Blinds {
    require(smallBlind <= Int.MAX_VALUE / 2) { "Small blind can't be that large." }
    require(smallBlind >= 1) { "Small blind must be greater than 0." }
    require(bigBlind >= 2) { "Big blind must be greater than 0." }
    require(bigBlind == smallBlind * 2) { "The big blind must be twice as large as the small blind." }
    return Blinds(smallBlind, bigBlind)
}

data class BlindSchedule (
    val numberOfMinutesBetweenLevels: Int,
    val currentLevel: Int) {

    companion object {
        fun init(numberOfMinutesBetweenLevels: Int): BlindSchedule {
            return BlindSchedule(numberOfMinutesBetweenLevels, 1)
        }
    }

    init {
        require(currentLevel > 0) { "current level must be greater than zero" }
        require(currentLevel <= maxLevel()) { "current level must be less than or equal to the maxLevel" }
    }

    fun maxLevel(): Int {
        return levelToBlindsMap.keys.maxOrNull()!!
    }

    fun isMaxLevel(): Boolean {
        return currentLevel == maxLevel()
    }

    fun currentBlinds(): Blinds {
        return levelToBlindsMap[currentLevel]!!
    }

    fun incrementBlinds(): BlindSchedule {
        return if (isMaxLevel()) this
        else copy(currentLevel = currentLevel.inc())
    }

}

data class Blinds (val smallBlind: Int, val bigBlind: Int)

fun applyEvents(events: List<GameEvent>): GameState {
    return applyEvents(null, events)
}

fun applyEvents(state: GameState?, events: List<GameEvent>): GameState {
    return events.fold(state, { acc, event -> applyEvent(acc, event) })!!
}

fun applyEvent(state: GameState?, event: GameEvent): GameState {
    require(state != null || event is GameCreatedEvent)
    return when(event) {
        is GameCreatedEvent -> GameState(
            event.aggregateId, event.numberOfPlayers, event.numberOfPlayersPerTable,
            GameStage.REGISTERING, BlindSchedule.init(event.numberOfMinutesBetweenBlindLevels)
        )
        is GameJoinedEvent -> state!!.copy(registeredPlayerIds = state.registeredPlayerIds.plus(event.playerId))
        is GameMovedToStartingStageEvent -> state!!.copy(stage = GameStage.STARTING)
        is GameStartedEvent -> state!!.copy(stage = GameStage.INPROGRESS)
        is GameTablesCreatedAndPlayersAssociatedEvent ->
            state!!.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plusAll(event.tableIdToPlayerIdsMap))
        is GameFinishedEvent -> state!!.copy(stage = GameStage.FINISHED)
        is NewHandIsClearedToStartEvent -> state!!
        is BlindsIncreasedEvent -> state!!.copy(blindSchedule = state.blindSchedule.incrementBlinds())
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
            val toPlayerIds = state.tableIdToPlayerIdsMap[event.toTableId]!!.plus(event.playerId)
            state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap
                .plus(event.fromTableId, fromPlayerIds)
                .plus(event.toTableId, toPlayerIds))
        }
        is PlayerBustedGameEvent -> {
            val tableId = state!!.tableIdToPlayerIdsMap.entries.first { it.value.contains(event.playerId) }.key
            val playerIds = state.tableIdToPlayerIdsMap[tableId]!!.minus(event.playerId)
            state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plus(tableId, playerIds))
        }
    }

}