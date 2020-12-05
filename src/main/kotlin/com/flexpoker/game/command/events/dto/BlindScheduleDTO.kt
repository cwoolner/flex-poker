package com.flexpoker.game.command.events.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class BlindScheduleDTO @JsonCreator constructor(
    @JsonProperty(value = "numberOfMinutesBetweenLevels") val numberOfMinutesBetweenLevels: Int,
    @JsonProperty(value = "levelToAmountsMap") levelToAmountsMap: Map<Int?, BlindAmountsDTO?>?,
    @JsonProperty(value = "maxLevel") maxLevel: Int,
    @JsonProperty(value = "currentLevel") currentLevel: Int
) {
    val levelToAmountsMap: Map<Int?, BlindAmountsDTO?>?
    val maxLevel: Int
    val currentLevel: Int

    init {
        this.levelToAmountsMap = levelToAmountsMap
        this.maxLevel = maxLevel
        this.currentLevel = currentLevel
    }
}