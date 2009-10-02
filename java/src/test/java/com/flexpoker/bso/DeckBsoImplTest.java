package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Context;


public class DeckBsoImplTest {

    private DeckBsoImpl bso = (DeckBsoImpl) Context.instance().getBean("deckBso");

    @Test
    public void testShuffleDeck() {
        Table table = setupTable();

        // just make sure that it doesn't throw an exception
        bso.shuffleDeck(table);
    }

    @Test
    public void testRemoveDeck() {
        Table table = setupTable();
        bso.shuffleDeck(table);

        // just make sure it doesn't throw an exception
        bso.removeDeck(table);
    }

    @Test
    public void testFetchFlopCards() {
        Table table = setupTable();
        bso.shuffleDeck(table);

        FlopCards flopCards = bso.fetchFlopCards(table);
        assertNotNull(flopCards);
        assertNotNull(flopCards.getCard1());
        assertNotNull(flopCards.getCard2());
        assertNotNull(flopCards.getCard3());
    }

    @Test
    public void testFetchPocketCards() {
        Table table = setupTable();
        bso.shuffleDeck(table);

        PocketCards pocketCards1 = bso.fetchPocketCards(table.getSeats().get(0)
                .getUserGameStatus().getUser(), table);
        PocketCards pocketCards2 = bso.fetchPocketCards(table.getSeats().get(1)
                .getUserGameStatus().getUser(), table);
        assertNotNull(pocketCards1);
        assertNotNull(pocketCards1.getCard1());
        assertNotNull(pocketCards1.getCard2());
        assertNotNull(pocketCards2);
        assertNotNull(pocketCards2.getCard1());
        assertNotNull(pocketCards2.getCard2());

        assertFalse(pocketCards1.getCard1().equals(pocketCards1.getCard2()));
        assertFalse(pocketCards1.getCard1().equals(pocketCards2.getCard1()));
        assertFalse(pocketCards1.getCard1().equals(pocketCards2.getCard2()));
        assertFalse(pocketCards1.getCard2().equals(pocketCards2.getCard1()));
        assertFalse(pocketCards1.getCard2().equals(pocketCards2.getCard2()));
        assertFalse(pocketCards2.getCard1().equals(pocketCards2.getCard2()));
    }

    @Test
    public void testFetchRiverCard() {
        Table table = setupTable();
        bso.shuffleDeck(table);

        RiverCard riverCard = bso.fetchRiverCard(table);
        assertNotNull(riverCard);
        assertNotNull(riverCard.getCard());
    }

    @Test
    public void testFetchTurnCard() {
        Table table = setupTable();
        bso.shuffleDeck(table);

        TurnCard turnCard = bso.fetchTurnCard(table);
        assertNotNull(turnCard);
        assertNotNull(turnCard.getCard());
    }

    private Table setupTable() {
        Table table = new Table();
        List<Seat> seats = new ArrayList<Seat>();
        Seat seat1 = new Seat();
        seat1.setPosition(0);
        UserGameStatus userGameStatus1 = new UserGameStatus();
        User user1 = new User();
        userGameStatus1.setUser(user1);
        seat1.setUserGameStatus(userGameStatus1);
        Seat seat2 = new Seat();
        seat2.setPosition(1);
        UserGameStatus userGameStatus2 = new UserGameStatus();
        User user2 = new User();
        userGameStatus1.setUser(user2);
        seat2.setUserGameStatus(userGameStatus2);

        seats.add(seat1);
        seats.add(seat2);
        table.setSeats(seats);
        return table;
    }

}
