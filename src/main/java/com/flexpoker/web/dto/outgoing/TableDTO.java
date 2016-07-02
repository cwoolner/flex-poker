package com.flexpoker.web.dto.outgoing;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDTO {

    private final UUID tableId;

    private final int version;

    private final List<SeatDTO> seats;

    private final int totalPot;

    private final Set<PotDTO> pots;

    private final List<CardDTO> visibleCommonCards;

    public TableDTO(UUID tableId, int version, List<SeatDTO> seats,
            int totalPot, Set<PotDTO> pots, List<CardDTO> visibleCommonCards) {
        this.tableId = tableId;
        this.version = version;
        this.seats = seats;
        this.totalPot = totalPot;
        this.pots = pots;
        this.visibleCommonCards = visibleCommonCards;
    }

    @JsonProperty
    public UUID getId() {
        return tableId;
    }

    @JsonProperty
    public int getVersion() {
        return version;
    }

    @JsonProperty
    public List<SeatDTO> getSeats() {
        return seats;
    }

    @JsonProperty
    public int getTotalPot() {
        return totalPot;
    }

    @JsonProperty
    public Set<PotDTO> getPots() {
        return pots;
    }

    @JsonProperty
    public List<CardDTO> getVisibleCommonCards() {
        return visibleCommonCards;
    }

}
