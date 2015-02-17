package com.flexpoker.web.model.outgoing;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDTO {

    private final UUID tableId;

    @JsonProperty
    private List<SeatDTO> seats;

    @JsonProperty
    private int totalPot;

    @JsonProperty
    private Set<PotDTO> pots;

    @JsonProperty
    private List<CardDTO> visibleCommonCards;

    public TableDTO(UUID tableId, List<SeatDTO> seats, int totalPot,
            Set<PotDTO> pots, List<CardDTO> visibleCommonCards) {
        this.tableId = tableId;
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
        this.visibleCommonCards = visibleCommonCards;
    }

    public UUID getId() {
        return tableId;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public int getTotalPot() {
        return totalPot;
    }

    public void setTotalPot(int totalPot) {
        this.totalPot = totalPot;
    }

    public Set<PotDTO> getPots() {
        return pots;
    }

    public void setPots(Set<PotDTO> pots) {
        this.pots = pots;
    }

    public List<CardDTO> getVisibleCommonCards() {
        return visibleCommonCards;
    }

    public void setVisibleCommonCards(List<CardDTO> visibleCommonCards) {
        this.visibleCommonCards = visibleCommonCards;
    }

}
