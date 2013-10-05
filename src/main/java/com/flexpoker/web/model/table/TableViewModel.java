package com.flexpoker.web.model.table;

import java.util.List;
import java.util.Set;

public class TableViewModel {

    private List<SeatViewModel> seats;

    private int totalPot;

    private Set<PotViewModel> pots;

    public TableViewModel(List<SeatViewModel> seats, int totalPot, Set<PotViewModel> pots) {
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
    }

    public List<SeatViewModel> getSeats() {
        return seats;
    }

    public int getTotalPot() {
        return totalPot;
    }

    public Set<PotViewModel> getPots() {
        return pots;
    }

}
