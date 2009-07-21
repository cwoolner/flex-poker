package com.flexpoker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deck {

    private List<Card> deckOfCards = new ArrayList<Card>(52);

    private Map<User, PocketCards> pocketCardsMap = new HashMap<User, PocketCards>();

    private FlopCards flopCards;

    private TurnCard turnCard;

    private RiverCard riverCard;

    public Deck(List<Card> cardList, Table table) {
        Collections.copy(deckOfCards, cardList);

        List<Seat> seats = new ArrayList<Seat>(table.getSeats());
        Collections.sort(seats);

        int dealerIndex = seats.indexOf(table.getButton());
        int numberOfPlayers = seats.size();

        determinePocketCards(seats, dealerIndex, numberOfPlayers);
        determineCommonCards(numberOfPlayers);
    }

    private void determinePocketCards(List<Seat> seats, int dealerIndex, int numberOfPlayers) {
        int firstDealtIndex = 0;

        // if the dealerIndex is anything besides the last index, then the first
        // dealt is the player to the left
        if (dealerIndex != numberOfPlayers) {
            firstDealtIndex = dealerIndex + 1;
        }

        int i = firstDealtIndex;
        int j = 0;

        while (i < numberOfPlayers) {
            Card pocketCard1 = deckOfCards.get(j);
            Card pocketCard2 = deckOfCards.get(j + numberOfPlayers);
            PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);
            pocketCardsMap.put(seats.get(i).getUser(), pocketCards);

            i++;
            j++;
        }

        i = 0;
        while (i < firstDealtIndex) {
            Card pocketCard1 = deckOfCards.get(j);
            Card pocketCard2 = deckOfCards.get(j + numberOfPlayers);
            PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);
            pocketCardsMap.put(seats.get(i).getUser(), pocketCards);

            i++;
            j++;
        }
    }

    private void determineCommonCards(int numberOfPlayers) {
        int flopCardsIndex = (numberOfPlayers * 2) + 1;
        flopCards = new FlopCards(
                deckOfCards.get(flopCardsIndex),
                deckOfCards.get(flopCardsIndex + 1),
                deckOfCards.get(flopCardsIndex + 2));
        turnCard = new TurnCard(deckOfCards.get(flopCardsIndex + 4));
        riverCard = new RiverCard(deckOfCards.get(flopCardsIndex + 6));
    }

    public Card getCard(int index) {
        return deckOfCards.get(index);
    }

    public FlopCards getFlopCards() {
        return flopCards;
    }

    public PocketCards getPocketCards(User user) {
        return pocketCardsMap.get(user);
    }

    public RiverCard getRiverCard() {
        return riverCard;
    }

    public TurnCard getTurnCard() {
        return turnCard;
    }

}
