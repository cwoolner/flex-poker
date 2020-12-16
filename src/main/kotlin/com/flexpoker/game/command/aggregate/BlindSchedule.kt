package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import java.util.Map

class BlindSchedule(numberOfMinutesBetweenLevels: Int) {
    var blindScheduleDTO: BlindScheduleDTO
        private set

    private fun validateBlindAmounts(smallBlind: Int, bigBlind: Int): BlindAmountsDTO {
        require(smallBlind <= Int.MAX_VALUE / 2) { "Small blind can't be that large." }
        require(smallBlind >= 1) { "Small blind must be greater than 0." }
        require(bigBlind >= 2) { "Big blind must be greater than 0." }
        require(bigBlind == smallBlind * 2) { "The big blind must be twice as large as the small blind." }
        return BlindAmountsDTO(smallBlind, bigBlind)
    }

    val numberOfMinutesBetweenLevels: Int
        get() = blindScheduleDTO.numberOfMinutesBetweenLevels
    val currentBlindAmounts: BlindAmountsDTO
        get() = blindScheduleDTO.levelToAmountsMap[blindScheduleDTO.currentLevel]!!
    val currentLevel: Int
        get() = blindScheduleDTO.currentLevel

    fun incrementLevel() {
        if (blindScheduleDTO.currentLevel < blindScheduleDTO.maxLevel) {
            blindScheduleDTO = BlindScheduleDTO(
                blindScheduleDTO.numberOfMinutesBetweenLevels,
                blindScheduleDTO.levelToAmountsMap,
                blindScheduleDTO.maxLevel,
                blindScheduleDTO.currentLevel + 1
            )
        }
    }

    val isMaxLevel: Boolean
        get() = blindScheduleDTO.currentLevel == blindScheduleDTO.maxLevel

    init {
        val levelToAmountsMap = Map.of(
            1, validateBlindAmounts(10, 20),
            2, validateBlindAmounts(20, 40),
            3, validateBlindAmounts(40, 80),
            4, validateBlindAmounts(80, 160),
            5, validateBlindAmounts(160, 320)
        )
        val maxLevel = levelToAmountsMap.keys.maxOrNull()!!

        blindScheduleDTO = BlindScheduleDTO(numberOfMinutesBetweenLevels, levelToAmountsMap, maxLevel, 1)
    }
}