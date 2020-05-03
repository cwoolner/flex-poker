package com.flexpoker.web.dto.outgoing;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDTO {

    private final UUID tableId;

    private final int version;

    private final List<SeatDTO> seats;

    private final int totalPot;

    private final Set<PotDTO> pots;

    private final List<CardDTO> visibleCommonCards;

    private final int currentHandMinRaiseToAmount;

    private final UUID currentHandId;

    @JsonCreator
    public TableDTO(
            @JsonProperty(value = "id") UUID tableId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "seats") List<SeatDTO> seats,
            @JsonProperty(value = "totalPot") int totalPot,
            @JsonProperty(value = "pots") Set<PotDTO> pots,
            @JsonProperty(value = "visibleCommonCards") List<CardDTO> visibleCommonCards,
            @JsonProperty(value = "currentHandMinRaiseToAmount") int currentHandMinRaiseToAmount,
            @JsonProperty(value = "currentHandId") UUID currentHandId) {
        this.tableId = tableId;
        this.version = version;
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
        this.visibleCommonCards = visibleCommonCards;
        this.currentHandMinRaiseToAmount = currentHandMinRaiseToAmount;
        this.currentHandId = currentHandId;
    }

    public UUID getId() {
        return tableId;
    }

    public int getVersion() {
        return version;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public int getTotalPot() {
        return totalPot;
    }

    public Set<PotDTO> getPots() {
        return pots;
    }

    public List<CardDTO> getVisibleCommonCards() {
        return visibleCommonCards;
    }

    public int getCurrentHandMinRaiseToAmount() {
        return currentHandMinRaiseToAmount;
    }

    public UUID getCurrentHandId() {
        return currentHandId;
    }

}
