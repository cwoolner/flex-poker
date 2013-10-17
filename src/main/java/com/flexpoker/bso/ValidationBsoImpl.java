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

}
