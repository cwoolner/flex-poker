package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import com.flexpoker.game.command.events.dto.BlindScheduleDTO

private val levelToAmountsMap = mapOf(
    1 to validateBlindAmounts(10, 20),
    2 to validateBlindAmounts(20, 40),
    3 to validateBlindAmounts(40, 80),
    4 to validateBlindAmounts(80, 160),
    5 to validateBlindAmounts(160, 320)
)

fun blindSchedule(numberOfMinutesBetweenLevels: Int): BlindScheduleDTO {
    val maxLevel = levelToAmountsMap.keys.maxOrNull()!!
    return BlindScheduleDTO(numberOfMinutesBetweenLevels, levelToAmountsMap, maxLevel, 1)
}

private fun validateBlindAmounts(smallBlind: Int, bigBlind: Int): BlindAmountsDTO {
    require(smallBlind <= Int.MAX_VALUE / 2) { "Small blind can't be that large." }
    require(smallBlind >= 1) { "Small blind must be greater than 0." }
    require(bigBlind >= 2) { "Big blind must be greater than 0." }
    require(bigBlind == smallBlind * 2) { "The big blind must be twice as large as the small blind." }
    return BlindAmountsDTO(smallBlind, bigBlind)
}

fun numberOfMinutesBetweenLevels(state: GameState): Int {
    return state.blindScheduleDTO.numberOfMinutesBetweenLevels
}

fun currentBlindAmounts(state: GameState): BlindAmountsDTO {
    return state.blindScheduleDTO.levelToAmountsMap[state.blindScheduleDTO.currentLevel]!!
}

fun currentLevel(state: GameState): Int {
    return state.blindScheduleDTO.currentLevel
}

fun incrementLevel(state: GameState): GameState {
    val blindScheduleDTO = state.blindScheduleDTO
    return if (isMaxLevel(state)) state
    else {
        state.copy(blindScheduleDTO = blindScheduleDTO.copy(currentLevel = blindScheduleDTO.currentLevel.inc()))
    }
}

fun isMaxLevel(state: GameState): Boolean {
   return state.blindScheduleDTO.currentLevel == state.blindScheduleDTO.maxLevel
}