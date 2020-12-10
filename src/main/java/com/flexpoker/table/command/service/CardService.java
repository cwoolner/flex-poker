package com.flexpoker.table.command.service;

import java.util.List;

import com.flexpoker.table.command.Card;
import com.flexpoker.table.command.CardsUsedInHand;

public interface CardService {

    List<Card> createShuffledDeck();

    CardsUsedInHand createCardsUsedInHand(List<Card> fullDeckOfCards, int numberOfPlayers);

}
