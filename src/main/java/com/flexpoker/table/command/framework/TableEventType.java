package com.flexpoker.table.command.framework;

import com.flexpoker.framework.event.EventType;

public enum TableEventType implements EventType {

    TableCreated, CardsShuffled, HandDealtEvent, //

    PlayerChecked, PlayerRaised, PlayerCalled, PlayerFolded, //

    FlopCardsDealt, TurnCardDealt, RiverCardDealt, //

    PotCreated, PotAmountIncreased, PotClosed, //

    RoundCompleted, ActionOnChanged, LastToActChanged, WinnersDetermined, HandCompleted

}
