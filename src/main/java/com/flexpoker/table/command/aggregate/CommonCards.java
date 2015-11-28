package com.flexpoker.table.command.aggregate;

import java.util.Arrays;
import java.util.List;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public class CommonCards {

    private final List<Card> cards;

    public CommonCards(FlopCards flopCards, TurnCard turnCard,
            RiverCard riverCard) {
        cards = Arrays.asList(new Card[] { flopCards.getCard1(),
                flopCards.getCard2(), flopCards.getCard3(), turnCard.getCard(),
                riverCard.getCard() });
    }

    public List<Card> getCards() {
        return cards;
    }

    public FlopCards getFlopCards() {
        return new FlopCards(cards.get(0), cards.get(1), cards.get(2));
    }

    public TurnCard getTurnCard() {
        return new TurnCard(cards.get(3));
    }

    public RiverCard getRiverCard() {
        return new RiverCard(cards.get(4));
    }

}
