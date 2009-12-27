package com.flexpoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.Authentication;

import com.flexpoker.bso.RealTimeGameBsoImpl;
import com.flexpoker.controller.FlexControllerImpl;
import com.flexpoker.dao.UserDaoImpl;
import com.flexpoker.dao.GameDaoImpl;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.IntegrationContext;
import com.flexpoker.util.IntegrationTest;

/**
 * Integration test for FlexControllerImpl class.
 *
 * @author cwoolner
 */
public class FlexControllerImplIntTest extends IntegrationTest {

    private UserDaoImpl userDao = (UserDaoImpl) IntegrationContext.instance()
            .getBean("userDao");

    private GameDaoImpl gameDao = (GameDaoImpl) IntegrationContext.instance()
            .getBean("gameDao");

    private FlexControllerImpl flexController = (FlexControllerImpl)
            IntegrationContext.instance().getBean("flexController");

    private RealTimeGameBsoImpl realTimeGameBso = (RealTimeGameBsoImpl)
            IntegrationContext.instance().getBean("realTimeGameBso");

    @Test
    public void testCheck() {
        setEntityManagers(userDao, gameDao);
        User john = userDao.findByUsername("john").get(0);
        User guest = userDao.findByUsername("guest").get(0);

        Authentication johnAuth = new UsernamePasswordAuthenticationToken(john, "");
        Authentication guestAuth = new UsernamePasswordAuthenticationToken(guest, "");
        SecurityContextHolder.getContext().setAuthentication(johnAuth);

        Game game = new Game();
        game.setTotalPlayers(2);
        game.setMaxPlayersPerTable(2);

        entityTransaction.begin();
        flexController.createGame(game);
        entityTransaction.commit();

        flexController.joinGame(game);
        SecurityContextHolder.getContext().setAuthentication(guestAuth);
        flexController.joinGame(game);

        assertEquals(2, flexController.fetchAllUserGameStatuses(game).size());

        flexController.verifyRegistrationForGame(game);
        SecurityContextHolder.getContext().setAuthentication(johnAuth);
        flexController.verifyRegistrationForGame(game);

        flexController.verifyGameInProgress(game);
        SecurityContextHolder.getContext().setAuthentication(guestAuth);
        flexController.verifyGameInProgress(game);

        Table table = flexController.fetchTable(game);

        PocketCards johnPocketCards = flexController.fetchPocketCards(game, table);
        SecurityContextHolder.getContext().setAuthentication(johnAuth);
        PocketCards guestPocketCards = flexController.fetchPocketCards(game, table);

        assertNotNull(johnPocketCards);
        assertNotNull(guestPocketCards);
        assertFalse(johnPocketCards.equals(guestPocketCards));

        table = flexController.fetchTable(game);
        RealTimeHand realTimeHand = realTimeGameBso.get(game).getRealTimeHand(table);

        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());
        Seat johnSeat;
        Seat guestSeat;

        if (table.getSeats().get(0).getUserGameStatus().getUser().equals(john)) {
            johnSeat = table.getSeats().get(0);
            guestSeat = table.getSeats().get(1);
        } else {
            johnSeat = table.getSeats().get(1);
            guestSeat = table.getSeats().get(0);
        }

        if (actionOnSeat.equals(johnSeat)) {
            testCheckCommon(johnSeat, guestSeat, johnAuth, guestAuth, game,
                    table, realTimeHand);
        } else if (actionOnSeat.equals(guestSeat)) {
            testCheckCommon(guestSeat, johnSeat, guestAuth, johnAuth, game,
                    table, realTimeHand);
        } else {
            fail("Neither seat belongs to guest or john.");
        }
    }

    private void testCheckCommon(Seat initialActionOnSeat, Seat initialActionOffSeat,
            Authentication actionOnAuth, Authentication actionOffAuth,
            Game game, Table table, RealTimeHand realTimeHand) {
            commonChipCheck(initialActionOnSeat, 1490, 10, 10, 40);
            commonChipCheck(initialActionOffSeat, 1480, 0, 20, 40);
            assertTrue(table.getTotalPotAmount() == 30);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat,
                    GameEventType.CALL, GameEventType.FOLD, GameEventType.RAISE);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.CHECK, GameEventType.RAISE);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat,
                    GameEventType.CHECK);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.FOLD, GameEventType.CALL);

            // initial call from the small blind
            SecurityContextHolder.getContext().setAuthentication(actionOnAuth);
            flexController.call(game, table);

            commonChipCheck(initialActionOnSeat, 1480, 0, 20, 0);
            commonChipCheck(initialActionOffSeat, 1480, 0, 20, 40);
            assertTrue(table.getTotalPotAmount() == 40);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.CHECK, GameEventType.RAISE);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat,
                    GameEventType.CHECK, GameEventType.CALL, GameEventType.FOLD,
                    GameEventType.RAISE);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.FOLD, GameEventType.CALL);

            // big blind checks
            SecurityContextHolder.getContext().setAuthentication(actionOffAuth);
            flexController.check(game, table);

            commonChipCheck(initialActionOnSeat, 1480, 0, 0, 20);
            commonChipCheck(initialActionOffSeat, 1480, 0, 0, 20);
            assertTrue(table.getTotalPotAmount() == 40);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat,
                    GameEventType.CHECK, GameEventType.RAISE);
            commonAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.CHECK, GameEventType.RAISE);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOnSeat,
                    GameEventType.FOLD, GameEventType.CALL);
            commonNotAllowedToPerformActionCheck(realTimeHand, initialActionOffSeat,
                    GameEventType.FOLD, GameEventType.CALL);
    }

    private void commonAllowedToPerformActionCheck(RealTimeHand realTimeHand,
            Seat seat, GameEventType...gameEventTypes) {
        for (GameEventType gameEventType : gameEventTypes) {
            assertTrue(realTimeHand.isUserAllowedToPerformAction(gameEventType, seat));
        }
    }

    private void commonNotAllowedToPerformActionCheck(RealTimeHand realTimeHand,
            Seat seat, GameEventType...gameEventTypes) {
        for (GameEventType gameEventType : gameEventTypes) {
            assertFalse(realTimeHand.isUserAllowedToPerformAction(gameEventType, seat));
        }
    }

    private void commonChipCheck(Seat seat, int chips, int callAmount,
            int chipsInFront, int raiseTo) {
        assertTrue(seat.getUserGameStatus().getChips() == chips);
        assertTrue(seat.getCallAmount() == callAmount);
        assertTrue(seat.getChipsInFront() == chipsInFront);
        assertTrue(seat.getRaiseTo() == raiseTo);
    }

}
