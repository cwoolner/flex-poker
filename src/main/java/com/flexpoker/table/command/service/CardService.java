package com.flexpoker.table.command.service;

import java.util.List;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;

public interface CardService {

    List<Card> createShuffledDeck();

    CardsUsedInHand createCardsUsedInHand(List<Card> fullDeckOfCards, int numberOfPlayers);

}
