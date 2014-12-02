package com.flexpoker.core.pot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.model.card.CardRank;

public class DeterminePotWinnersImplQueryTest {

    private DeterminePotWinnersImplQuery query;

    @Before
    public void setup() {
        query = new DeterminePotWinnersImplQuery();
    }

    @Test
    public void testSetWinners() {
        Table table = new Table();

        User user1 = new User(UUID.randomUUID(), "test1");
        User user2 = new User(UUID.randomUUID(), "test2");
        User user3 = new User(UUID.randomUUID(), "test3");

        UserGameStatus userGameStatus1 = new UserGameStatus(user1, 1500);
        UserGameStatus userGameStatus2 = new UserGameStatus(user2, 1500);
        UserGameStatus userGameStatus3 = new UserGameStatus(user3, 1500);

        testSetWinners1(table, user1, user2, userGameStatus1, userGameStatus2);
        testSetWinners2(table, user1, user2, user3, userGameStatus1, userGameStatus2,
                userGameStatus3);
    }

    private void testSetWinners1(Table table, User user1, User user2,
            UserGameStatus userGameStatus1, UserGameStatus userGameStatus2) {

        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        seat1.setUserGameStatus(userGameStatus1);
        Seat seat2 = new Seat(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setUserGameStatus(userGameStatus2);

        table.addSeat(seat1);
        table.addSeat(seat2);

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(user1.getId());
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(user2.getId());
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat1);
        seats.add(seat2);

        Set<Seat> winners = query.execute(table, seats, winningHands);

        assertEquals(1, winners.size());
        assertTrue(winners.contains(seat1));
    }

    private void testSetWinners2(Table table, User user1, User user2, User user3,
            UserGameStatus userGameStatus1, UserGameStatus userGameStatus2,
            UserGameStatus userGameStatus3) {

        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        seat1.setUserGameStatus(userGameStatus1);
        Seat seat2 = new Seat(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setUserGameStatus(userGameStatus2);
        Seat seat3 = new Seat(2);
        seat3.setChipsInFront(30);
        seat3.setStillInHand(true);
        seat3.setUserGameStatus(userGameStatus3);

        table.addSeat(seat1);
        table.addSeat(seat2);
        table.addSeat(seat3);

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(user1.getId());
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(user2.getId());
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);
        HandEvaluation handEvaluation3 = new HandEvaluation();
        handEvaluation3.setPlayerId(user3.getId());
        handEvaluation3.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation3.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);
        winningHands.add(handEvaluation3);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat2);
        seats.add(seat3);

        Set<Seat> winners = query.execute(table, seats, winningHands);

        assertEquals(2, winners.size());
        assertTrue(winners.contains(seat2));
        assertTrue(winners.contains(seat3));
    }

}
