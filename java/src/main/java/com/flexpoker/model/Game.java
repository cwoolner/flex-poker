package com.flexpoker.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

    private Integer id;

    private Date startTime;

    private Date endTime;

    private User winningUser;

    private User createdByUser;

    private Date createdOn;

    private Date canceledOn;

    private Integer totalPlayers;

    private Integer playersRemaining;

    private Integer minimumPlayers;

    private Integer maximumPlayers;

    private Boolean allowRebuys;

    private List<UserStatusInGame> userStatusInGames;

    private GameStage gameStage;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @OneToOne
    @JoinColumn(name = "winner")
    public User getWinningUser() {
        return winningUser;
    }

    public void setWinningUser(User winningUser) {
        this.winningUser = winningUser;
    }

    @OneToOne
    @JoinColumn(name = "createdBy")
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

    @OneToMany
    @JoinColumn(name = "gameId")
    public List<UserStatusInGame> getUserStatusInGames() {
        return userStatusInGames;
    }

    public void setUserStatusInGames(List<UserStatusInGame> userStatusInGames) {
        this.userStatusInGames = userStatusInGames;
    }

    @ManyToOne
    @JoinColumn(name = "gameStageId")
    public GameStage getGameStage() {
        return gameStage;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
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
