package com.flexpoker.web.translator.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
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
        
        for (PotViewModel potViewModel : potViewModels) {
            totalPot += potViewModel.getAmount();
        }
        
        return new TableViewModel(seatViewModels, totalPot, potViewModels);
    }
}
