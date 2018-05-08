package com.flexpoker.table.command.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardSuit;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

@Service
public class DefaultCardService implements CardService {

    @Override
    public List<Card> createShuffledDeck() {
        var deckOfCards = createDeckOfCards();
        Collections.shuffle(deckOfCards, new Random());
        return deckOfCards;
    }

    @Override
    public CardsUsedInHand createCardsUsedInHand(List<Card> fullDeckOfCards,
            int numberOfPlayers) {
        var flopCardsIndex = (numberOfPlayers * 2) + 1;
        var flopCards = new FlopCards(fullDeckOfCards.get(flopCardsIndex),
                fullDeckOfCards.get(flopCardsIndex + 1),
                fullDeckOfCards.get(flopCardsIndex + 2));
        var turnCard = new TurnCard(fullDeckOfCards.get(flopCardsIndex + 4));
        var riverCard = new RiverCard(fullDeckOfCards.get(flopCardsIndex + 6));

        var pocketCards = new ArrayList<PocketCards>();

        for (int i = 0; i < numberOfPlayers; i++) {
            var pocketCard1 = fullDeckOfCards.get(i);
            var pocketCard2 = fullDeckOfCards.get(i + numberOfPlayers);
            pocketCards.add(new PocketCards(pocketCard1, pocketCard2));
        }

        return new CardsUsedInHand(flopCards, turnCard, riverCard, pocketCards);
    }

    private List<Card> createDeckOfCards() {
        return Arrays.asList(new Card[] { new Card(0, CardRank.TWO, CardSuit.HEARTS),
                new Card(1, CardRank.THREE, CardSuit.HEARTS),
                new Card(2, CardRank.FOUR, CardSuit.HEARTS),
                new Card(3, CardRank.FIVE, CardSuit.HEARTS),
                new Card(4, CardRank.SIX, CardSuit.HEARTS),
                new Card(5, CardRank.SEVEN, CardSuit.HEARTS),
                new Card(6, CardRank.EIGHT, CardSuit.HEARTS),
                new Card(7, CardRank.NINE, CardSuit.HEARTS),
                new Card(8, CardRank.TEN, CardSuit.HEARTS),
                new Card(9, CardRank.JACK, CardSuit.HEARTS),
                new Card(10, CardRank.QUEEN, CardSuit.HEARTS),
                new Card(11, CardRank.KING, CardSuit.HEARTS),
                new Card(12, CardRank.ACE, CardSuit.HEARTS),
                new Card(13, CardRank.TWO, CardSuit.SPADES),
                new Card(14, CardRank.THREE, CardSuit.SPADES),
                new Card(15, CardRank.FOUR, CardSuit.SPADES),
                new Card(16, CardRank.FIVE, CardSuit.SPADES),
                new Card(17, CardRank.SIX, CardSuit.SPADES),
                new Card(18, CardRank.SEVEN, CardSuit.SPADES),
                new Card(19, CardRank.EIGHT, CardSuit.SPADES),
                new Card(20, CardRank.NINE, CardSuit.SPADES),
                new Card(21, CardRank.TEN, CardSuit.SPADES),
                new Card(22, CardRank.JACK, CardSuit.SPADES),
                new Card(23, CardRank.QUEEN, CardSuit.SPADES),
                new Card(24, CardRank.KING, CardSuit.SPADES),
                new Card(25, CardRank.ACE, CardSuit.SPADES),
                new Card(26, CardRank.TWO, CardSuit.DIAMONDS),
                new Card(27, CardRank.THREE, CardSuit.DIAMONDS),
                new Card(28, CardRank.FOUR, CardSuit.DIAMONDS),
                new Card(29, CardRank.FIVE, CardSuit.DIAMONDS),
                new Card(30, CardRank.SIX, CardSuit.DIAMONDS),
                new Card(31, CardRank.SEVEN, CardSuit.DIAMONDS),
                new Card(32, CardRank.EIGHT, CardSuit.DIAMONDS),
                new Card(33, CardRank.NINE, CardSuit.DIAMONDS),
                new Card(34, CardRank.TEN, CardSuit.DIAMONDS),
                new Card(35, CardRank.JACK, CardSuit.DIAMONDS),
                new Card(36, CardRank.QUEEN, CardSuit.DIAMONDS),
                new Card(37, CardRank.KING, CardSuit.DIAMONDS),
                new Card(38, CardRank.ACE, CardSuit.DIAMONDS),
                new Card(39, CardRank.TWO, CardSuit.CLUBS),
                new Card(40, CardRank.THREE, CardSuit.CLUBS),
                new Card(41, CardRank.FOUR, CardSuit.CLUBS),
                new Card(42, CardRank.FIVE, CardSuit.CLUBS),
                new Card(43, CardRank.SIX, CardSuit.CLUBS),
                new Card(44, CardRank.SEVEN, CardSuit.CLUBS),
                new Card(45, CardRank.EIGHT, CardSuit.CLUBS),
                new Card(46, CardRank.NINE, CardSuit.CLUBS),
                new Card(47, CardRank.TEN, CardSuit.CLUBS),
                new Card(48, CardRank.JACK, CardSuit.CLUBS),
                new Card(49, CardRank.QUEEN, CardSuit.CLUBS),
                new Card(50, CardRank.KING, CardSuit.CLUBS),
                new Card(51, CardRank.ACE, CardSuit.CLUBS) });
    }

}
