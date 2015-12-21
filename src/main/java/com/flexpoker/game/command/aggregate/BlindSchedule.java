package com.flexpoker.game.command.aggregate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BlindSchedule {

    private final int numberOfMinutesBetweenLevels;

    private final Map<Integer, BlindAmounts> levelToAmountsMap;

    private final int maxLevel;

    private int currentLevel;

    public BlindSchedule(int numberOfMinutesBetweenLevels) {
        this.numberOfMinutesBetweenLevels = numberOfMinutesBetweenLevels;
        levelToAmountsMap = new HashMap<>();
        levelToAmountsMap.put(1, new BlindAmounts(10, 20));
        levelToAmountsMap.put(2, new BlindAmounts(20, 40));
        levelToAmountsMap.put(3, new BlindAmounts(40, 80));
        levelToAmountsMap.put(4, new BlindAmounts(80, 160));
        levelToAmountsMap.put(5, new BlindAmounts(160, 320));
        maxLevel = levelToAmountsMap.keySet().stream()
                .max(Comparator.naturalOrder()).get();
        currentLevel = 1;
    }

    public int getNumberOfMinutesBetweenLevels() {
        return numberOfMinutesBetweenLevels;
    }

    public BlindAmounts getCurrentBlindAmounts() {
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

}
