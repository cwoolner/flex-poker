package com.flexpoker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cardSuits")
public class CardSuit {

    public static final String HEARTS = "hearts";

    public static final String SPADES = "spades";

    public static final String DIAMONDS = "diamonds";

    public static final String CLUBS = "clubs";

    private Integer id;

    private String name;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
