package com.flexpoker.table.command.framework;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flexpoker.framework.event.Event;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionOnChangedEvent.class, name = "ActionOnChanged"),
        @JsonSubTypes.Type(value = CardsShuffledEvent.class, name = "CardsShuffled"),
        @JsonSubTypes.Type(value = FlopCardsDealtEvent.class, name = "FlopCardsDealt"),
        @JsonSubTypes.Type(value = HandCompletedEvent.class, name = "HandCompleted"),
        @JsonSubTypes.Type(value = HandDealtEvent.class, name = "HandDealt"),
        @JsonSubTypes.Type(value = LastToActChangedEvent.class, name = "LastToActChanged"),
        @JsonSubTypes.Type(value = PlayerCalledEvent.class, name = "PlayerCalled"),
        @JsonSubTypes.Type(value = PlayerCheckedEvent.class, name = "PlayerChecked"),
        @JsonSubTypes.Type(value = PlayerFoldedEvent.class, name = "PlayerFolded"),
        @JsonSubTypes.Type(value = PlayerRaisedEvent.class, name = "PlayerRaised"),
        @JsonSubTypes.Type(value = RiverCardDealtEvent.class, name = "RiverCardDealt"),
        @JsonSubTypes.Type(value = TableCreatedEvent.class, name = "TableCreated"),
        @JsonSubTypes.Type(value = TurnCardDealtEvent.class, name = "TurnCardDealt") })
public interface TableEvent extends Event<TableEventType> {

    UUID getGameId();

}
