package com.flexpoker.bso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.CardDao;
import com.flexpoker.model.Card;
import com.flexpoker.model.Deck;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Transactional
@Service("deckBso")
public class DeckBsoImpl implements DeckBso {

    private Map<Table, Deck> tableToDeckMap = new HashMap<Table, Deck>();

    private List<Card> cardList;

    @Autowired
    public DeckBsoImpl(CardDao cardDao) {
        cardList = cardDao.findAll();
    }

    @Override
    public void shuffleDeck(Table table) {
        Deck deck = null;

        synchronized (this) {
            try {
                Thread.currentThread().sleep(10);
                Collections.shuffle(cardList, new Random());
                deck = new Deck(cardList, table);
            } catch (InterruptedException e) {
                throw new RuntimeException("InterruptedException thrown while "
                        + "generating random number.");
            }
        }

        synchronized (tableToDeckMap) {
            tableToDeckMap.put(table, deck);
        }

    }

    @Override
    public void removeDeck(Table table) {
        synchronized (tableToDeckMap) {
            tableToDeckMap.remove(table);
        }
    }

    @Override
    public FlopCards fetchFlopCards(Table table) {
        return tableToDeckMap.get(table).getFlopCards();
    }

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        return tableToDeckMap.get(table).getPocketCards(user);
    }

    @Override
    public RiverCard fetchRiverCard(Table table) {
        return tableToDeckMap.get(table).getRiverCard();
    }

    @Override
    public TurnCard fetchTurnCard(Table table) {
        return tableToDeckMap.get(table).getTurnCard();
    }

}
