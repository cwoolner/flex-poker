package com.flexpoker.web.controller;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.PlayerActionsBso;
import com.flexpoker.web.model.table.handaction.BaseHandActionViewModel;
import com.flexpoker.web.model.table.handaction.RaiseHandActionViewModel;

@Controller
public class HandActionController {
    
    private final PlayerActionsBso playerActionsBso;
    
    @Inject
    public HandActionController(PlayerActionsBso playerActionsBso) {
        this.playerActionsBso = playerActionsBso;
    }

    @MessageMapping(value = "/app/check")
    public void check(BaseHandActionViewModel model, Principal principal) {
        UUID gameUUID = UUID.fromString(model.getGameId());
        UUID tableUUID = UUID.fromString(model.getTableId());
        playerActionsBso.check(gameUUID, tableUUID, principal);
    }

    @MessageMapping(value = "/app/fold")
    public void fold(BaseHandActionViewModel model, Principal principal) {
        UUID gameUUID = UUID.fromString(model.getGameId());
        UUID tableUUID = UUID.fromString(model.getTableId());
        playerActionsBso.fold(gameUUID, tableUUID, principal);
    }

    @MessageMapping(value = "/app/call")
    public void call(BaseHandActionViewModel model, Principal principal) {
        UUID gameUUID = UUID.fromString(model.getGameId());
        UUID tableUUID = UUID.fromString(model.getTableId());
        playerActionsBso.call(gameUUID, tableUUID, principal);
    }

    @MessageMapping(value = "/app/raise")
    public void raise(RaiseHandActionViewModel model, Principal principal) {
        UUID gameUUID = UUID.fromString(model.getGameId());
        UUID tableUUID = UUID.fromString(model.getTableId());
        playerActionsBso.raise(gameUUID, tableUUID, model.getRaiseToAmount(), principal);
    }

}
