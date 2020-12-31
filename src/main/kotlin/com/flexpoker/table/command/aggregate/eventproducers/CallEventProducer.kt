package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.checkActionOnPlayer
import com.flexpoker.table.command.aggregate.checkPerformAction
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun call(state: HandState, playerId: UUID): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.CALL)
    return listOf(PlayerCalledEvent(state.tableId, state.gameId, state.entityId, playerId))
}