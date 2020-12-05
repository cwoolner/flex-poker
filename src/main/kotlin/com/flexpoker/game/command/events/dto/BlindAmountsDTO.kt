package com.flexpoker.game.command.events.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class BlindAmountsDTO @JsonCreator constructor(
    @param:JsonProperty(value = "smallBlind") val smallBlind: Int,
    @param:JsonProperty(value = "bigBlind") val bigBlind: Int
)