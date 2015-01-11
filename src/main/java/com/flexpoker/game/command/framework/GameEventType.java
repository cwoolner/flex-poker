package com.flexpoker.game.command.framework;

import com.flexpoker.framework.event.EventType;

public enum GameEventType implements EventType {

    GameCreated, GameJoined, GameMovedToStartingStage, GameTablesCreatedAndPlayersAssociated, //

    GameStarted, GameFinished, NewHandIsClearedToStart

}
