package com.flexpoker.game.command.aggregate;

import java.util.Comparator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.game.command.events.dto.BlindAmountsDTO;

public class BlindSchedule {

    private final int numberOfMinutesBetweenLevels;

    private final Map<Integer, BlindAmountsDTO> levelToAmountsMap;

    private final int maxLevel;

    private int currentLevel;

    @JsonCreator
    public BlindSchedule(@JsonProperty(value = "numberOfMinutesBetweenLevels") int numberOfMinutesBetweenLevels) {
        this.numberOfMinutesBetweenLevels = numberOfMinutesBetweenLevels;
        levelToAmountsMap = Map.of(
                1, validateBlindAmounts(10, 20),
                2, validateBlindAmounts(20, 40),
                3, validateBlindAmounts(40, 80),
                4, validateBlindAmounts(80, 160),
                5, validateBlindAmounts(160, 320));
        maxLevel = levelToAmountsMap.keySet().stream()
                .max(Comparator.naturalOrder()).get();
        currentLevel = 1;
    }

    private BlindAmountsDTO validateBlindAmounts(int smallBlind, int bigBlind) {
        if (smallBlind > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Small blind can't be that large.");
        }
        if (smallBlind < 1) {
            throw new IllegalArgumentException("Small blind must be greater than 0.");
        }
        if (bigBlind < 2) {
            throw new IllegalArgumentException("Big blind must be greater than 0.");
        }
        if (bigBlind != smallBlind * 2) {
            throw new IllegalArgumentException("The big blind must be twice as "
                    + "large as the small blind.");
        }
        return new BlindAmountsDTO(smallBlind, bigBlind);
    }

    public int getNumberOfMinutesBetweenLevels() {
        return numberOfMinutesBetweenLevels;
    }

    public BlindAmountsDTO getCurrentBlindAmounts() {
        return levelToAmountsMap.get(currentLevel);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void incrementLevel() {
        if (currentLevel < maxLevel) {
            currentLevel++;
        }
    }

    public boolean isMaxLevel() {
        return currentLevel == maxLevel;
    }

}
