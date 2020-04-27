package com.flexpoker.game.command.framework;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flexpoker.framework.event.Event;
import com.flexpoker.game.command.events.BlindsIncreasedEvent;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameFinishedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BlindsIncreasedEvent.class, name = "BlindsIncreased"),
        @JsonSubTypes.Type(value = GameCreatedEvent.class, name = "GameCreated"),
        @JsonSubTypes.Type(value = GameFinishedEvent.class, name = "GameFinished"),
        @JsonSubTypes.Type(value = GameJoinedEvent.class, name = "GameJoined"),
        @JsonSubTypes.Type(value = GameMovedToStartingStageEvent.class, name = "GameMovedToStartingStage"),
        @JsonSubTypes.Type(value = GameStartedEvent.class, name = "GameStarted"),
        @JsonSubTypes.Type(value = GameTablesCreatedAndPlayersAssociatedEvent.class, name = "GameTablesCreatedAndPlayersAssociated"),
        @JsonSubTypes.Type(value = NewHandIsClearedToStartEvent.class, name = "NewHandIsClearedToStart"),
        @JsonSubTypes.Type(value = PlayerBustedGameEvent.class, name = "PlayerBustedGame"),
        @JsonSubTypes.Type(value = PlayerMovedToNewTableEvent.class, name = "PlayerMovedToNewTable"),
        @JsonSubTypes.Type(value = TablePausedForBalancingEvent.class, name = "TablePausedForBalancing"),
        @JsonSubTypes.Type(value = TableRemovedEvent.class, name = "TableRemoved"),
        @JsonSubTypes.Type(value = TableResumedAfterBalancingEvent.class, name = "TableResumedAfterBalancing") })
public interface GameEvent extends Event {

}
