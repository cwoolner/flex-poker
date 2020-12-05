package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class GameTablesCreatedAndPlayersAssociatedEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @JsonProperty(value = "tableIdToPlayerIdsMap") tableIdToPlayerIdsMap: Map<UUID, Set<UUID>>?,
    @JsonProperty(value = "numberOfPlayersPerTable") numberOfPlayersPerTable: Int
) : BaseGameEvent(gameId), GameEvent {
    private val tableIdToPlayerIdsMap: Map<UUID, Set<UUID>>
    val numberOfPlayersPerTable: Int
    fun getTableIdToPlayerIdsMap(): Map<UUID, Set<UUID>> {
        return HashMap(tableIdToPlayerIdsMap)
    }

    init {
        this.tableIdToPlayerIdsMap = HashMap(tableIdToPlayerIdsMap)
        this.numberOfPlayersPerTable = numberOfPlayersPerTable
    }
}