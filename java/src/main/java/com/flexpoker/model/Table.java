
package com.flexpoker.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@javax.persistence.Table(name = "tables")
public class Table {

    private Integer id;

    private Game game;

    private Seat button;

    private Seat smallBlind;

    private Seat bigBlind;

    private List<Seat> seats;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "gameId")
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @OneToOne
    @JoinColumn(name = "buttonId")
    public Seat getButton() {
        return button;
    }

    public void setButton(Seat button) {
        this.button = button;
    }

    @OneToOne
    @JoinColumn(name = "smallBlindId")
    public Seat getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(Seat smallBlind) {
        this.smallBlind = smallBlind;
    }

    @OneToOne
    @JoinColumn(name = "bigBlindId")
    public Seat getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(Seat bigBlind) {
        this.bigBlind = bigBlind;
    }

    @OneToMany
    @JoinColumn(name = "tableId")
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
