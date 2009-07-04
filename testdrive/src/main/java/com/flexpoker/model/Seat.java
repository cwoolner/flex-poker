
package com.flexpoker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {

    private com.flexpoker.model.Table table;

    private User user;

    private Integer position;

    @OneToOne
    @Column(name = "tableId")
    public com.flexpoker.model.Table getTable() {
        return table;
    }

    public void setTable(com.flexpoker.model.Table table) {
        this.table = table;
    }

    @OneToOne
    @Column(name = "userId")
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

}
