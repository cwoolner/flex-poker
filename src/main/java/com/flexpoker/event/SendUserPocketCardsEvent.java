package com.flexpoker.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.flexpoker.model.card.PocketCards;

public class SendUserPocketCardsEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3013201610851250782L;

    private final String username;

    private final PocketCards pocketCards;

    private final UUID tableId;

    public SendUserPocketCardsEvent(Object source, String username,
            PocketCards pocketCards, UUID tableId) {
        super(source);
        this.username = username;
        this.pocketCards = pocketCards;
        this.tableId = tableId;
    }

    public String getUsername() {
        return username;
    }

    public PocketCards getPocketCards() {
        return pocketCards;
    }

    public UUID getTableId() {
        return tableId;
    }
}
