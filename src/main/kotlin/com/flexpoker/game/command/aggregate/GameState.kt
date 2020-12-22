package com.flexpoker.game.command.aggregate

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
    val numberOfMinutesBetweenBlindLevels: Int,
    val registeredPlayerIds: PSet<UUID> = HashTreePSet.empty(),
    val tableIdToPlayerIdsMap: PMap<UUID, PSet<UUID>> = HashTreePMap.empty(),
    val pausedTablesForBalancing: PSet<UUID> = HashTreePSet.empty()
)