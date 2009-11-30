package com.flexpoker.model;

import java.util.Date;

public class UserGameStatus {

    private User user;

    private Integer minBet;

    private Integer callAmount;

    private Integer chips;

    private Date enterTime;

    private Date exitTime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMinBet() {
        return minBet;
    }

    public void setMinBet(Integer minBet) {
        this.minBet = minBet;
    }

    public Integer getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(Integer callAmount) {
        this.callAmount = callAmount;
    }

    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

}
