package com.flexpoker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gameEventTypes")
public class GameEventType {

    public static final String HAND_DEALT = "hand dealt";

    public static final String CHECK = "check";
    
    public static final String FOLD = "fold";

    public static final String BET = "bet";

    public static final String RAISE = "raise";

    public static final String HAND_COMPLETE = "hand complete";

    public static final String WON_HAND = "won hand";

    public static final String PLAYER_OUT = "player out";

    public static final String FLOP_DEALT = "flop dealt";

    public static final String TURN_DEALT = "turn dealt";

    public static final String RIVER_DEALT = "river dealt";

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
