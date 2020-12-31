package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun expireActionOn(state: HandState, playerId: UUID): List<TableEvent> {
    return if (state.callAmountsMap[playerId]!! == 0) check(state, playerId, true)
    else fold(state, playerId, true)
}