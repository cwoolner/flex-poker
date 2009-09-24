package com.flexpoker.model;

import java.util.List;

public class Table {

    private Integer id;

    private Seat button;

    private Seat smallBlind;

    private Seat bigBlind;

    private Seat actionOn;

    private List<Seat> seats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seat getButton() {
        return button;
    }

    public void setButton(Seat button) {
        this.button = button;
    }

    public Seat getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(Seat smallBlind) {
        this.smallBlind = smallBlind;
    }

    public Seat getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(Seat bigBlind) {
        this.bigBlind = bigBlind;
    }

    public Seat getActionOn() {
        return actionOn;
    }

    public void setActionOn(Seat actionOn) {
        this.actionOn = actionOn;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Table other = (Table) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
