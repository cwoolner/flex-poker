package com.flexpoker.web.translator.table;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.web.model.table.PotViewModel;

public class PotTranslator {

    public PotViewModel translate(Pot pot) {
        
        @SuppressWarnings("unchecked")
        Set<String> seats = new HashSet<>(CollectionUtils.collect(pot.getSeats(), new Transformer() {
            @Override
            public String transform(Object seat) {
                return ((Seat) seat).getUserGameStatus().getUser().getUsername();
            }
        }));
        
        @SuppressWarnings("unchecked")
        Set<String> winners = new HashSet<>(CollectionUtils.collect(pot.getWinners(), new Transformer() {
            @Override
            public String transform(Object seat) {
                return ((Seat) seat).getUserGameStatus().getUser().getUsername();
            }
        }));

        
        return new PotViewModel(seats, pot.getAmount(), pot.isOpen(), winners);
    }
}
