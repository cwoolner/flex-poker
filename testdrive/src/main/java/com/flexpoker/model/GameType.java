package com.flexpoker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gameTypes")
public class GameType {

    private Integer id;

    private String name;

    private Integer minimumPlayers;

    private Integer maximumPlayers;

    private Boolean allowRebuys;

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

    public Integer getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(Integer minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public Integer getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(Integer maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public Boolean getAllowRebuys() {
        return allowRebuys;
    }

    public void setAllowRebuys(Boolean allowRebuys) {
        this.allowRebuys = allowRebuys;
    }

}
