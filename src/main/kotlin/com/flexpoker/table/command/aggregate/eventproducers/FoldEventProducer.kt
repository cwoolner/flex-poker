package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.checkActionOnPlayer
import com.flexpoker.table.command.aggregate.checkPerformAction
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun fold(state: HandState, playerId: UUID, forced: Boolean): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.FOLD)
    return if (forced) listOf(PlayerForceFoldedEvent(state.tableId, state.gameId, state.entityId, playerId))
    else listOf(PlayerFoldedEvent(state.tableId, state.gameId, state.entityId, playerId))
}