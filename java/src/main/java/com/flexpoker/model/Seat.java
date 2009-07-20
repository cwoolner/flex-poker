package com.flexpoker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat implements Comparable<Seat> {

    private Integer id;

    private com.flexpoker.model.Table table;

    private User user;

    private Integer position;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "tableId")
    public com.flexpoker.model.Table getTable() {
        return table;
    }

    public void setTable(com.flexpoker.model.Table table) {
        this.table = table;
    }

    @OneToOne
    @JoinColumn(name = "userId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public int compareTo(Seat seat) {
        return position.compareTo(seat.getPosition());
    }

}
