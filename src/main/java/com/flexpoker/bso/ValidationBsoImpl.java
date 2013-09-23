package com.flexpoker.bso;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Constants;

@Service
public class ValidationBsoImpl implements ValidationBso {

    @Override
    public void validateTableAssignment(Set<UserGameStatus> userGameStatuses,
            int maxPlayersPerTable) {
        if (CollectionUtils.isEmpty(userGameStatuses)) {
            throw new IllegalArgumentException("userGameStatuses cannot be empty.");
        }
        if (maxPlayersPerTable > Constants.MAX_PLAYERS_PER_TABLE
                || maxPlayersPerTable < Constants.MIN_PLAYERS_PER_TABLE) {
            throw new IllegalArgumentException("Number of players must be "
                    + "between: " + Constants.MIN_PLAYERS_PER_TABLE
                    + " and " + Constants.MAX_PLAYERS_PER_TABLE);
        }
        if (maxPlayersPerTable == 2 && userGameStatuses.size() % 2 != 0) {
            throw new IllegalArgumentException("For a heads-up tournament, you "
                    + "must have an even number of players.");
        }
    }

    @Override
    public void validateRaiseAmount(int minimumAmount, int maximumAmount,
            String raiseAmount) {
        int raiseAmountInt;
        try {
            raiseAmountInt = Integer.parseInt(raiseAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Raise amount must be an integer.");
        }

        if (raiseAmountInt <= 0 || raiseAmountInt < minimumAmount
                || raiseAmountInt > maximumAmount) {
            throw new IllegalArgumentException("Raise amount must be between the "
                    + "minimum and maximum values.");
        }
    }

    @Override
    public void validateValuesAreNonNull(Object... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Vararg objects cannot be null.");
        }

        for (Object object : objects) {
            if (object == null) {
                throw new IllegalArgumentException("Object cannot be null.");
            }
        }
    }

}
