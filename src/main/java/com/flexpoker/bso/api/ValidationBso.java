package com.flexpoker.bso.api;

import java.util.Set;

import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;

/**
 * Interface that defines a set of methods to be used to check inputs for
 * exceptional situations.  Checks that are meant to return boolean-type
 * responses do not belong in this interface.  The methods provided here
 * should just act as a sanity check before other operations are performed.
 * If the rest of the application is working correctly, these methods should
 * never raise an exception, and therefore have no functional impact on the
 * system.
 *
 * @author cwoolner
 */
public interface ValidationBso {

    /**
     * Verify that the set of users to be assigned to tables is valid, and that
     * the maximum number of players per table is valid.
     */
    void validateTableAssignment(Set<UserGameStatus> userGameStatuses,
            int maxPlayersPerTable);

    /**
     * Verify that the raise amount is a valid number, and that it's between
     * the minimum and maximum amounts.
     */
    void validateRaiseAmount(int minimumAmount, int maximumAmount,
            String raiseAmount);

    /**
     * Verify that the values sent in are not null.
     *
     * @param objects
     */
    void validateValuesAreNonNull(Object... objects);

}
