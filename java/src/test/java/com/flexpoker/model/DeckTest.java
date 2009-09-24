package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class DeckTest {

    @Test
    public void testDeck() {
        List<Card> cardList = new ArrayList<Card>();
        Card card1 = new Card();
        card1.setId(1);
        Card card2 = new Card();
        card2.setId(2);
        Card card3 = new Card();
        card3.setId(3);
        Card card4 = new Card();
        card4.setId(4);
        Card card5 = new Card();
        card5.setId(5);
        Card card6 = new Card();
        card6.setId(6);
        Card card7 = new Card();
        card7.setId(7);
        Card card8 = new Card();
        card8.setId(8);
        Card card9 = new Card();
        card9.setId(9);
        Card card10 = new Card();
        card10.setId(10);
        Card card11 = new Card();
        card11.setId(11);
        Card card12 = new Card();
        card12.setId(12);
        Card card13 = new Card();
        card13.setId(13);
        Card card14 = new Card();
        card14.setId(14);
        Card card15 = new Card();
        card15.setId(15);
        
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        cardList.add(card6);
        cardList.add(card7);
        cardList.add(card8);
        cardList.add(card9);
        cardList.add(card10);
        cardList.add(card11);
        cardList.add(card12);
        cardList.add(card13);
        cardList.add(card14);
        cardList.add(card15);

        Table table = new Table();
        Set<Seat> seats = new HashSet<Seat>();
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        UserGameStatus userGameStatus1 = new UserGameStatus();
        UserGameStatus userGameStatus2 = new UserGameStatus();
        Seat seat1 = new Seat();
        seat1.setPosition(1);
        Seat seat2 = new Seat();
        seat2.setPosition(2);

        userGameStatus1.setUser(user1);
        userGameStatus2.setUser(user2);
        seat1.setUserGameStatus(userGameStatus1);
        seat2.setUserGameStatus(userGameStatus2);
        seats.add(seat1);
        seats.add(seat2);
        table.setSeats(seats);
        table.setButton(seat1);
        
        Deck deck = new Deck(cardList, table);
        PocketCards pocketCards1 = deck.getPocketCards(user1);
        PocketCards pocketCards2 = deck.getPocketCards(user2);
        FlopCards flopCards = deck.getFlopCards();
        TurnCard turnCard = deck.getTurnCard();
        RiverCard riverCard = deck.getRiverCard();

        assertTrue(pocketCards1.getCard1().getId() == 2);
        assertTrue(pocketCards1.getCard2().getId() == 4);
        assertTrue(pocketCards2.getCard1().getId() == 1);
        assertTrue(pocketCards2.getCard2().getId() == 3);
        assertTrue(flopCards.getCard1().getId() == 6);
        assertTrue(flopCards.getCard2().getId() == 7);
        assertTrue(flopCards.getCard3().getId() == 8);
        assertTrue(turnCard.getCard().getId() == 10);
        assertTrue(riverCard.getCard().getId() == 12);
        
        table.setButton(seat2);
        deck = new Deck(cardList, table);
        pocketCards1 = deck.getPocketCards(user1);
        pocketCards2 = deck.getPocketCards(user2);
        flopCards = deck.getFlopCards();
        turnCard = deck.getTurnCard();
        riverCard = deck.getRiverCard();

        assertTrue(pocketCards1.getCard1().getId() == 1);
        assertTrue(pocketCards1.getCard2().getId() == 3);
        assertTrue(pocketCards2.getCard1().getId() == 2);
        assertTrue(pocketCards2.getCard2().getId() == 4);
        assertTrue(flopCards.getCard1().getId() == 6);
        assertTrue(flopCards.getCard2().getId() == 7);
        assertTrue(flopCards.getCard3().getId() == 8);
        assertTrue(turnCard.getCard().getId() == 10);
        assertTrue(riverCard.getCard().getId() == 12);
    }

}
