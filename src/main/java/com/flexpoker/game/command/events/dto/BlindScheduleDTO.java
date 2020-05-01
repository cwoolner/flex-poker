package com.flexpoker.game.command.events.dto;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlindScheduleDTO {

    private final int numberOfMinutesBetweenLevels;

    private final Map<Integer, BlindAmountsDTO> levelToAmountsMap;

    private final int maxLevel;

    private final int currentLevel;

    @JsonCreator
    public BlindScheduleDTO(
            @JsonProperty(value = "numberOfMinutesBetweenLevels") int numberOfMinutesBetweenLevels,
            @JsonProperty(value = "levelToAmountsMap") Map<Integer, BlindAmountsDTO> levelToAmountsMap,
            @JsonProperty(value = "maxLevel") int maxLevel,
            @JsonProperty(value = "currentLevel") int currentLevel) {
        this.numberOfMinutesBetweenLevels = numberOfMinutesBetweenLevels;
        this.levelToAmountsMap = Collections.unmodifiableMap(levelToAmountsMap);
        this.maxLevel = maxLevel;
        this.currentLevel = currentLevel;
    }

    public int getNumberOfMinutesBetweenLevels() {
        return numberOfMinutesBetweenLevels;
    }

    public Map<Integer, BlindAmountsDTO> getLevelToAmountsMap() {
        return levelToAmountsMap;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

}
