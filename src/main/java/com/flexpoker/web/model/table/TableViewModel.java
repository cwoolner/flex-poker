package com.flexpoker.web.model.table;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableViewModel {

    @JsonProperty
    private List<SeatViewModel> seats;

    @JsonProperty
    private int totalPot;

    @JsonProperty
    private Set<PotViewModel> pots;

    @JsonProperty
    private List<CardViewModel> visibleCommonCards;

    public TableViewModel(List<SeatViewModel> seats, int totalPot,
            Set<PotViewModel> pots, List<CardViewModel> visibleCommonCards) {
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
        this.visibleCommonCards = visibleCommonCards;
    }

}
