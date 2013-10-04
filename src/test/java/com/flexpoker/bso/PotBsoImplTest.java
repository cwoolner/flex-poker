package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.model.CardRank;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

public class PotBsoImplTest {

    private PotBsoImpl bso;

    @Before
    public void setup() {
        bso = new PotBsoImpl();
    }
    
    @Test
    public void testCalculatePotsAfterRound() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        Table table = new Table();
        table.setId(UUID.randomUUID());

        testCalculatePotsAfterRound1(game, table);
        testCalculatePotsAfterRound2(game, table);
        testCalculatePotsAfterRound3(game, table);
        testCalculatePotsAfterRound4(game, table);
    }

    @Test
    public void testCreateNewHandPot() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        bso.createNewHandPot(game, table1);
        bso.createNewHandPot(game, table2);

        List<Pot> pots = bso.fetchAllPots(game, table1);
        assertTrue(pots.isEmpty());
        pots = bso.fetchAllPots(game, table2);
        assertTrue(pots.isEmpty());
    }

    @Test
    public void testFetchAllPots() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        Table table = new Table();
        table.setId(UUID.randomUUID());
        bso.createNewHandPot(game, table);
        assertEquals(0, bso.fetchAllPots(game, table).size());
    }

    @Test
    public void testRemoveSeatFromPots() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        Table table = new Table();
        table.setId(UUID.randomUUID());

        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        Seat seat3 = new Seat();
        seat3.setPosition(2);
        seat3.setChipsInFront(30);
        seat3.setStillInHand(true);
        Seat seat4 = new Seat();
        seat4.setPosition(3);
        seat4.setChipsInFront(30);
        seat4.setStillInHand(true);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);
        table.getSeats().add(seat3);
        table.getSeats().add(seat4);

        bso.calculatePotsAfterRound(game, table);

        List<Pot> pots = bso.fetchAllPots(game, table);
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));

        bso.removeSeatFromPots(game, table, seat1);
        pots = bso.fetchAllPots(game, table);
        assertFalse(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));

        bso.removeSeatFromPots(game, table, seat3);
        pots = bso.fetchAllPots(game, table);
        assertFalse(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertFalse(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));

        bso.removeSeatFromPots(game, table, seat4);
        pots = bso.fetchAllPots(game, table);
        assertFalse(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertFalse(pots.get(0).getSeats().contains(seat3));
        assertFalse(pots.get(0).getSeats().contains(seat4));
    }

    @Test
    public void testSetWinners() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        Table table = new Table();
        table.setId(UUID.randomUUID());

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        User user3 = new User();
        user3.setId(3);
        UserGameStatus userGameStatus1 = new UserGameStatus();
        userGameStatus1.setUser(user1);
        UserGameStatus userGameStatus2 = new UserGameStatus();
        userGameStatus2.setUser(user2);
        UserGameStatus userGameStatus3 = new UserGameStatus();
        userGameStatus3.setUser(user3);

        testSetWinners1(game, table, user1, user2, userGameStatus1,
                userGameStatus2);
        testSetWinners2(game, table, user1, user2, user3, userGameStatus1,
                userGameStatus2, userGameStatus3);
    }

    private void testCalculatePotsAfterRound1(Game game, Table table) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);

        bso.calculatePotsAfterRound(game, table);

        List<Pot> pots = bso.fetchAllPots(game, table);
        assertEquals(1, pots.size());
        assertEquals(30, pots.get(0).getAmount());
        assertTrue(pots.get(0).getSeats().contains(seat1));
    }

    private void testCalculatePotsAfterRound2(Game game, Table table) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);

        bso.calculatePotsAfterRound(game, table);

        List<Pot> pots = bso.fetchAllPots(game, table);
        assertEquals(1, pots.size());
        assertEquals(60, pots.get(0).getAmount());
        assertTrue(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
    }

    private void testCalculatePotsAfterRound3(Game game, Table table) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setAllIn(true);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);

        bso.calculatePotsAfterRound(game, table);

        List<Pot> pots = bso.fetchAllPots(game, table);
        assertEquals(1, pots.size());
        assertEquals(60, pots.get(0).getAmount());
        assertFalse(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
    }

    private void testCalculatePotsAfterRound4(Game game, Table table) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        Seat seat3 = new Seat();
        seat3.setPosition(2);
        seat3.setChipsInFront(30);
        seat3.setStillInHand(true);
        Seat seat4 = new Seat();
        seat4.setPosition(3);
        seat4.setChipsInFront(30);
        seat4.setStillInHand(true);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);
        table.getSeats().add(seat3);
        table.getSeats().add(seat4);

        // simulate preflop
        bso.calculatePotsAfterRound(game, table);
        List<Pot> pots = bso.fetchAllPots(game, table);
        assertEquals(1, pots.size());
        assertEquals(120, pots.get(0).getAmount());
        assertTrue(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));

        seat1.setChipsInFront(50);
        seat2.setChipsInFront(50);
        seat3.setChipsInFront(50);
        seat4.setChipsInFront(50);

        // simulate preturn
        bso.calculatePotsAfterRound(game, table);
        pots = bso.fetchAllPots(game, table);
        assertEquals(1, pots.size());
        assertEquals(320, pots.get(0).getAmount());
        assertTrue(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));

        seat1.setChipsInFront(20);
        seat1.setAllIn(true);
        seat2.setChipsInFront(40);
        seat2.setAllIn(true);
        seat3.setChipsInFront(90);
        seat4.setChipsInFront(90);

        // simulate preriver
        bso.calculatePotsAfterRound(game, table);
        pots = bso.fetchAllPots(game, table);
        assertEquals(3, pots.size());
        assertEquals(400, pots.get(0).getAmount());
        assertFalse(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));
        assertEquals(60, pots.get(1).getAmount());
        assertFalse(pots.get(1).isOpen());
        assertFalse(pots.get(1).getSeats().contains(seat1));
        assertTrue(pots.get(1).getSeats().contains(seat2));
        assertTrue(pots.get(1).getSeats().contains(seat3));
        assertTrue(pots.get(1).getSeats().contains(seat4));
        assertEquals(100, pots.get(2).getAmount());
        assertTrue(pots.get(2).isOpen());
        assertFalse(pots.get(2).getSeats().contains(seat1));
        assertFalse(pots.get(2).getSeats().contains(seat2));
        assertTrue(pots.get(2).getSeats().contains(seat3));
        assertTrue(pots.get(2).getSeats().contains(seat4));

        seat3.setChipsInFront(100);
        seat3.setAllIn(true);
        seat4.setChipsInFront(400);

        // simulate last round
        bso.calculatePotsAfterRound(game, table);
        pots = bso.fetchAllPots(game, table);
        assertEquals(4, pots.size());
        assertEquals(400, pots.get(0).getAmount());
        assertFalse(pots.get(0).isOpen());
        assertTrue(pots.get(0).getSeats().contains(seat1));
        assertTrue(pots.get(0).getSeats().contains(seat2));
        assertTrue(pots.get(0).getSeats().contains(seat3));
        assertTrue(pots.get(0).getSeats().contains(seat4));
        assertEquals(60, pots.get(1).getAmount());
        assertFalse(pots.get(1).isOpen());
        assertFalse(pots.get(1).getSeats().contains(seat1));
        assertTrue(pots.get(1).getSeats().contains(seat2));
        assertTrue(pots.get(1).getSeats().contains(seat3));
        assertTrue(pots.get(1).getSeats().contains(seat4));
        assertEquals(300, pots.get(2).getAmount());
        assertFalse(pots.get(2).isOpen());
        assertFalse(pots.get(2).getSeats().contains(seat1));
        assertFalse(pots.get(2).getSeats().contains(seat2));
        assertTrue(pots.get(2).getSeats().contains(seat3));
        assertTrue(pots.get(2).getSeats().contains(seat4));
        assertEquals(300, pots.get(3).getAmount());
        assertTrue(pots.get(3).isOpen());
        assertFalse(pots.get(3).getSeats().contains(seat1));
        assertFalse(pots.get(3).getSeats().contains(seat2));
        assertFalse(pots.get(3).getSeats().contains(seat3));
        assertTrue(pots.get(3).getSeats().contains(seat4));
    }

    private void testSetWinners1(Game game, Table table, User user1, User user2,
            UserGameStatus userGameStatus1, UserGameStatus userGameStatus2) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        seat1.setUserGameStatus(userGameStatus1);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setUserGameStatus(userGameStatus2);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);

        bso.calculatePotsAfterRound(game, table);

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setUser(user1);
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setUser(user2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<HandEvaluation>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);

        bso.setWinners(game, table, winningHands);

        List<Pot> pots = bso.fetchAllPots(game, table);

        assertEquals(1, pots.get(0).getWinners().size());
        assertTrue(pots.get(0).getWinners().contains(seat1));
    }

    private void testSetWinners2(Game game, Table table, User user1, User user2,
            User user3,  UserGameStatus userGameStatus1,
            UserGameStatus userGameStatus2, UserGameStatus userGameStatus3) {
        bso.createNewHandPot(game, table);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        seat1.setUserGameStatus(userGameStatus1);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setUserGameStatus(userGameStatus2);
        Seat seat3 = new Seat();
        seat3.setPosition(2);
        seat3.setChipsInFront(30);
        seat3.setStillInHand(true);
        seat3.setUserGameStatus(userGameStatus3);

        table.setSeats(new ArrayList<Seat>());
        table.getSeats().add(seat1);
        table.getSeats().add(seat2);
        table.getSeats().add(seat3);

        bso.calculatePotsAfterRound(game, table);
        bso.removeSeatFromPots(game, table, seat1);

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setUser(user1);
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setUser(user2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);
        HandEvaluation handEvaluation3 = new HandEvaluation();
        handEvaluation3.setUser(user3);
        handEvaluation3.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation3.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<HandEvaluation>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);
        winningHands.add(handEvaluation3);

        bso.setWinners(game, table, winningHands);

        List<Pot> pots = bso.fetchAllPots(game, table);

        assertEquals(2, pots.get(0).getWinners().size());
        assertTrue(pots.get(0).getWinners().contains(seat2));
        assertTrue(pots.get(0).getWinners().contains(seat3));
    }

}
