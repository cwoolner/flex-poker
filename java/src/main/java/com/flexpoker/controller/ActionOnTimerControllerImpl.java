package com.flexpoker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.flexpoker.bso.ActionOnTimerBso;
import com.flexpoker.bso.PlayerActionsBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Controller("actionOnTimerController")
public class ActionOnTimerControllerImpl implements ActionOnTimerController {

    private EventManager eventManager;

    private PlayerActionsBso playerActionsBso;

    private ActionOnTimerBso actionOnTimerBso;

    @Override
    public void decrementTime() {
        Map<Game, Map<Table, Map<Seat, Integer>>> actionOnTimerMap =
                actionOnTimerBso.getActionOnTimerMap();

        for (Game game : actionOnTimerMap.keySet()) {
            for (Table table : actionOnTimerMap.get(game).keySet()) {
                Map<Seat, Integer> seatMap = actionOnTimerMap.get(game).get(table);
                if (seatMap.size() != 1) {
                    throw new IllegalArgumentException("Timer should only be on "
                            + "one user per table.");
                }
                for (Seat seat : seatMap.keySet()) {
                    Integer currentCount = seatMap.get(seat);
                    if (currentCount == 0) {
                        HandState handState = playerActionsBso.fold(game, table,
                                seat.getUserGameStatus().getUser());
                        eventManager.sendFoldEvent(game, table, handState,
                                seat.getUserGameStatus().getUser().getUsername());
                    } else {
                        currentCount = currentCount - 1;
                        seatMap.put(seat, currentCount);
                    }
                }
            }
        }
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public PlayerActionsBso getPlayerActionsBso() {
        return playerActionsBso;
    }

    public void setPlayerActionsBso(PlayerActionsBso playerActionsBso) {
        this.playerActionsBso = playerActionsBso;
    }

    public ActionOnTimerBso getActionOnTimerBso() {
        return actionOnTimerBso;
    }

    public void setActionOnTimerBso(ActionOnTimerBso actionOnTimerBso) {
        this.actionOnTimerBso = actionOnTimerBso;
    }

}
