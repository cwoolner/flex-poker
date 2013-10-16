package com.flexpoker.controller;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.simp.annotation.SubscribeEvent;

import com.flexpoker.bso.api.PlayerActionsBso;

public class HandActionController {
    
    private final PlayerActionsBso playerActionsBso;
    
    @Inject
    public HandActionController(PlayerActionsBso playerActionsBso) {
        this.playerActionsBso = playerActionsBso;
    }

    @SubscribeEvent(value = "/app/check")
    public void check(UUID gameId, UUID tableId, Principal principal) {
        playerActionsBso.check(gameId, tableId, principal);
    }

    @SubscribeEvent(value = "/app/fold")
    public void fold(UUID gameId, UUID tableId, Principal principal) {
        playerActionsBso.fold(gameId, tableId, principal);
    }

    @SubscribeEvent(value = "/app/call")
    public void call(UUID gameId, UUID tableId, Principal principal) {
        playerActionsBso.call(gameId, tableId, principal);
    }

    @SubscribeEvent(value = "/app/raise")
    public void raise(UUID gameId, UUID tableId, int raiseToAmount, Principal principal) {
        playerActionsBso.raise(gameId, tableId, raiseToAmount, principal);
    }

}
