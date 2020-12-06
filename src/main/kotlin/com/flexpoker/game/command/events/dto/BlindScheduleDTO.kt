package com.flexpoker.game.command.events.dto

data class BlindScheduleDTO (
    val numberOfMinutesBetweenLevels: Int,
    val levelToAmountsMap: Map<Int, BlindAmountsDTO>,
    val maxLevel: Int,
    val currentLevel: Int)