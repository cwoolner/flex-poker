package com.flexpoker.web.model.table;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableViewModel {

    private final UUID tableId;

    @JsonProperty
    private List<SeatViewModel> seats;

    @JsonProperty
    private int totalPot;

    @JsonProperty
    private Set<PotViewModel> pots;

    @JsonProperty
    private List<CardViewModel> visibleCommonCards;

    public TableViewModel(UUID tableId, List<SeatViewModel> seats, int totalPot,
            Set<PotViewModel> pots, List<CardViewModel> visibleCommonCards) {
        this.tableId = tableId;
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
        this.visibleCommonCards = visibleCommonCards;
    }

    public UUID getId() {
        return tableId;
    }

    public List<SeatViewModel> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatViewModel> seats) {
        this.seats = seats;
    }

    public int getTotalPot() {
        return totalPot;
    }

    public void setTotalPot(int totalPot) {
        this.totalPot = totalPot;
    }

    public Set<PotViewModel> getPots() {
        return pots;
    }

    public void setPots(Set<PotViewModel> pots) {
        this.pots = pots;
    }

    public List<CardViewModel> getVisibleCommonCards() {
        return visibleCommonCards;
    }

    public void setVisibleCommonCards(List<CardViewModel> visibleCommonCards) {
        this.visibleCommonCards = visibleCommonCards;
    }

    public UUID getTableId() {
        return tableId;
    }

}
