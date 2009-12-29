package com.flexpoker.bso.mock;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.ValidationBso;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;

@Service
public class ValidationBsoMock implements ValidationBso {

    @Override
    public void validateTable(Table table) {}

    @Override
    public void validateTableAssignment(Set<UserGameStatus> userGameStatuses,
            int maxPlayersPerTable) {}

    @Override
    public void validateRaiseAmount(int minimumAmount, int maximumAmount,
            String raiseAmount) {}

    @Override
    public void validateValuesAreNonNull(Object... objects) {}

}
