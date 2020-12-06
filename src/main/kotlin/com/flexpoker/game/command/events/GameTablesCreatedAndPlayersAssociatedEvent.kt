package com.flexpoker.game.command.events

import java.util.UUID

data class GameTablesCreatedAndPlayersAssociatedEvent (
    val gameId: UUID,
    val tableIdToPlayerIdsMap: Map<UUID, Set<UUID>>,
    val numberOfPlayersPerTable: Int) : BaseGameEvent(gameId)