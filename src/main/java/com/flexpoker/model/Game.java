package com.flexpoker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Game {

    private UUID id;
    
    private String name;

    private Date startTime;

    private Date endTime;

    private User winningUser;

    private User createdByUser;

    private Date createdOn;

    private Date canceledOn;

    private Integer totalPlayers;

    private Integer playersRemaining;

    private Integer maxPlayersPerTable;

    private Boolean allowRebuys;

    private GameStage gameStage;

    private Blinds currentBlinds;

    private List<Table> tables;

    private Set<UserGameStatus> userGameStatuses;

    public Game() {
        currentBlinds = new Blinds(10, 20);
        tables = new ArrayList<>();
        userGameStatuses = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getWinningUser() {
        return winningUser;
    }

    public void setWinningUser(User winningUser) {
        this.winningUser = winningUser;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCanceledOn() {
        return canceledOn;
    }

    public void setCanceledOn(Date canceledOn) {
        this.canceledOn = canceledOn;
    }

    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(Integer totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public Integer getPlayersRemaining() {
        return playersRemaining;
    }

    public void setPlayersRemaining(Integer playersRemaining) {
        this.playersRemaining = playersRemaining;
    }

    public Boolean getAllowRebuys() {
        return allowRebuys;
    }

    public void setAllowRebuys(Boolean allowRebuys) {
        this.allowRebuys = allowRebuys;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public Integer getMaxPlayersPerTable() {
        return maxPlayersPerTable;
    }

    public void setMaxPlayersPerTable(Integer maxPlayersPerTable) {
        this.maxPlayersPerTable = maxPlayersPerTable;
    }

    public Blinds getCurrentBlinds() {
        return currentBlinds;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        table.setId(UUID.randomUUID());
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

    public Table getTable(final UUID tableId) {
        Object table = CollectionUtils.find(tables, new Predicate() {

            @Override
            public boolean evaluate(Object table) {
                return ((Table) table).getId().equals(tableId);
            }
        });

        if (table == null) {
            return null;
        }

        return (Table) table;
    }

    public void addUserGameStatus(UserGameStatus userGameStatus) {
        userGameStatuses.add(userGameStatus);
    }

    public Set<UserGameStatus> getUserGameStatuses() {
        return userGameStatuses;
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
        Game other = (Game) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
