package com.flexpoker.model;

public class TableMovement {

    private Table fromTable;

    private Table toTable;

    private UserGameStatus userGameStatus;;

    public Table getFromTable() {
        return fromTable;
    }

    public void setFromTable(Table fromTable) {
        this.fromTable = fromTable;
    }

    public Table getToTable() {
        return toTable;
    }

    public void setToTable(Table toTable) {
        this.toTable = toTable;
    }

    public UserGameStatus getUserGameStatus() {
        return userGameStatus;
    }

    public void setUserGameStatus(UserGameStatus userGameStatus) {
        this.userGameStatus = userGameStatus;
    }

}
