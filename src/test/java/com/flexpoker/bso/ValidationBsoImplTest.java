package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.DataUtilsForTests;

public class ValidationBsoImplTest {

    private ValidationBsoImpl bso = new ValidationBsoImpl();

    @Test
    public void testValidateTableAssignment() {
        Set<UserGameStatus> userGameStatuses = null;
        try {
            bso.validateTableAssignment(userGameStatuses, 9);
            fail("An exception should have been thrown.  Can't send in an empty Set.");
        } catch (IllegalArgumentException e) {}

        userGameStatuses = DataUtilsForTests.createUserGameStatusSet(0);
        try {
            bso.validateTableAssignment(userGameStatuses, 9);
            fail("An exception should have been thrown.  Can't send in an empty Set.");
        } catch (IllegalArgumentException e) {}

        userGameStatuses = DataUtilsForTests.createUserGameStatusSet(7);
        try {
            bso.validateTableAssignment(userGameStatuses, 10);
            fail("An exception should have been thrown.  10 is too large of a table.");
        } catch (IllegalArgumentException e) {}
        try {
            bso.validateTableAssignment(userGameStatuses, 1);
            fail("An exception should have been thrown.  1 is too small of a table.");
        } catch (IllegalArgumentException e) {}
        try {
            bso.validateTableAssignment(userGameStatuses, 2);
            fail("A heads-up tournament must start with an even number of people.");
        } catch (IllegalArgumentException e) {}
    }

}
