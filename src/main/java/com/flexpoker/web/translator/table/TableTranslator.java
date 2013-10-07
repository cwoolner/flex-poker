package com.flexpoker.web.translator.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.web.model.table.CardViewModel;
import com.flexpoker.web.model.table.PotViewModel;
import com.flexpoker.web.model.table.SeatViewModel;
import com.flexpoker.web.model.table.TableViewModel;

public class TableTranslator {

    public TableViewModel translate(Table table) {
        List<SeatViewModel> seatViewModels = new ArrayList<>();
        for (Seat seat : table.getSeats()) {
            seatViewModels.add(new SeatTranslator().translate(seat));
        }
        
        Set<PotViewModel> potViewModels = new HashSet<>();
        for (Pot pot : table.getCurrentHand().getPots()) {
            potViewModels.add(new PotTranslator().translate(pot));
        }
        
        int totalPot = 0;
        
        for (Pot pot : table.getCurrentHand().getPots()) {
            totalPot += pot.getAmount();
        }
        
        List<CardViewModel> visibleCommonCards = new ArrayList<>();
        
        if (table.getCurrentHand().getHandDealerState().ordinal()
                >= HandDealerState.FLOP_DEALT.ordinal()) {
            FlopCards flopCards = table.getCurrentHand().getDeck().getFlopCards();
            visibleCommonCards.add(new CardViewModel(flopCards.getCard1().getId()));
            visibleCommonCards.add(new CardViewModel(flopCards.getCard2().getId()));
            visibleCommonCards.add(new CardViewModel(flopCards.getCard3().getId()));
        }
        
        if (table.getCurrentHand().getHandDealerState().ordinal()
                >= HandDealerState.TURN_DEALT.ordinal()) {
            TurnCard turnCard = table.getCurrentHand().getDeck().getTurnCard();
            visibleCommonCards.add(new CardViewModel(turnCard.getCard().getId()));
        }
        
        if (table.getCurrentHand().getHandDealerState().ordinal()
                >= HandDealerState.RIVER_DEALT.ordinal()) {
            RiverCard riverCard = table.getCurrentHand().getDeck().getRiverCard();
            visibleCommonCards.add(new CardViewModel(riverCard.getCard().getId()));
        }
        
        return new TableViewModel(seatViewModels, totalPot, potViewModels, visibleCommonCards);
    }
}
