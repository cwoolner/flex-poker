package com.flexpoker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.flexpoker.game.command.aggregate.BlindAmounts;
import com.flexpoker.game.query.dto.GameStage;

public class Game {

    private final UUID id;
    
    private final String name;

    private final Date startTime;

    private Date endTime;

    private User winningUser;

    private final User createdByUser;

    private final Date createdOn;

    private Date canceledOn;

    private final int totalPlayers;

    private int playersRemaining;

    private final int maxPlayersPerTable;

    private final boolean allowRebuys;

    private GameStage gameStage;

    private BlindAmounts currentBlinds;

    private final List<Table> tables;

    private Set<UserGameStatus> userGameStatuses;

    public Game(String name, Date startTime, User createdByUser, Date createdOn,
            int totalPlayers, int maxPlayersPerTable, boolean allowRebuys) {
        currentBlinds = new BlindAmounts(10, 20);
        tables = new ArrayList<>();
        userGameStatuses = new HashSet<>();
        id = UUID.randomUUID();
        gameStage = GameStage.REGISTERING;
        this.name = name;
        this.startTime = startTime;
        this.createdByUser = createdByUser;
        this.createdOn = createdOn;
        this.totalPlayers = totalPlayers;
        this.allowRebuys = allowRebuys;
        this.maxPlayersPerTable = maxPlayersPerTable;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
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

    public Date getCreatedOn() {
        return createdOn;
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

    public Integer getPlayersRemaining() {
        return playersRemaining;
    }

    public void setPlayersRemaining(Integer playersRemaining) {
        this.playersRemaining = playersRemaining;
    }

    public Boolean getAllowRebuys() {
        return allowRebuys;
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

    public BlindAmounts getCurrentBlinds() {
        return currentBlinds;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

    public Table getTable(final UUID tableId) {
        return tables.stream().filter(x -> x.getId().equals(tableId)).findAny()
                .orElse(null);
    }

    public void addUserGameStatus(UserGameStatus userGameStatus) {
        userGameStatuses.add(userGameStatus);
    }

    public Set<UserGameStatus> getUserGameStatuses() {
        return userGameStatuses;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Game rhs = (Game) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(id, rhs.id).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
