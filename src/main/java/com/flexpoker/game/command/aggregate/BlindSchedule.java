package com.flexpoker.game.command.aggregate;

import java.util.Comparator;
import java.util.Map;

import com.flexpoker.game.command.events.dto.BlindAmountsDTO;
import com.flexpoker.game.command.events.dto.BlindScheduleDTO;

public class BlindSchedule {

    private BlindScheduleDTO blindScheduleDTO;

    public BlindSchedule(int numberOfMinutesBetweenLevels) {
        var levelToAmountsMap = Map.of(
                1, validateBlindAmounts(10, 20),
                2, validateBlindAmounts(20, 40),
                3, validateBlindAmounts(40, 80),
                4, validateBlindAmounts(80, 160),
                5, validateBlindAmounts(160, 320));
        var maxLevel = levelToAmountsMap.keySet().stream()
                .max(Comparator.naturalOrder()).get();
        this.blindScheduleDTO = new BlindScheduleDTO(numberOfMinutesBetweenLevels,
                levelToAmountsMap, maxLevel, 1);
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
        return blindScheduleDTO.getNumberOfMinutesBetweenLevels();
    }

    public BlindAmountsDTO getCurrentBlindAmounts() {
        return blindScheduleDTO.getLevelToAmountsMap()
                .get(blindScheduleDTO.getCurrentLevel());
    }

    public int getCurrentLevel() {
        return blindScheduleDTO.getCurrentLevel();
    }

    public void incrementLevel() {
        if (blindScheduleDTO.getCurrentLevel() < blindScheduleDTO.getMaxLevel()) {
            this.blindScheduleDTO = new BlindScheduleDTO(
                    blindScheduleDTO.getNumberOfMinutesBetweenLevels(),
                    blindScheduleDTO.getLevelToAmountsMap(),
                    blindScheduleDTO.getMaxLevel(),
                    blindScheduleDTO.getCurrentLevel() + 1);
        }
    }

    public boolean isMaxLevel() {
        return blindScheduleDTO.getCurrentLevel() == blindScheduleDTO.getMaxLevel();
    }

    public BlindScheduleDTO getBlindScheduleDTO() {
        return blindScheduleDTO;
    }

}
