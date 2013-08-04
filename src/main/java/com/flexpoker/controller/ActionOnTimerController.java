package com.flexpoker.controller;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import com.flexpoker.bso.ActionOnTimerBso;
import com.flexpoker.bso.PlayerActionsBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Controller
public class ActionOnTimerController {

    @Inject
    private EventManager eventManager;

    @Inject
    private PlayerActionsBso playerActionsBso;

    @Inject
    private ActionOnTimerBso actionOnTimerBso;

    public void decrementTime() {
        synchronized (this) {
            Map<Game, Map<Table, Map<Seat, Integer>>> actionOnTimerMap =
                    actionOnTimerBso.getActionOnTimerMap();

            for (Game game : actionOnTimerMap.keySet()) {
                for (Table table : actionOnTimerMap.get(game).keySet()) {
                    Map<Seat, Integer> seatMap = actionOnTimerMap.get(game).get(table);
                    if (seatMap.size() > 1) {
                        throw new IllegalArgumentException("Timer should only be on "
                                + "one user per table.");
                    }
                    handleSeat(game, table, seatMap);
                }
            }
        }
    }

    private void handleSeat(Game game, Table table, Map<Seat, Integer> seatMap) {
        for (Seat seat : seatMap.keySet()) {
            Integer currentCount = seatMap.get(seat);
            if (currentCount == 0) {
                actionOnTimerBso.removeSeat(game, table, seat);

                if (seat.getCallAmount() == 0) {
                    // if they only need to check, then just check
                    HandState handState = playerActionsBso.check(game, table,
                            seat.getUserGameStatus().getUser());
                    eventManager.sendCheckEvent(game, table, handState,
                            seat.getUserGameStatus().getUser().getUsername());
                } else {
                    // otherwise fold them
                    HandState handState = playerActionsBso.fold(game, table,
                            seat.getUserGameStatus().getUser());
                    eventManager.sendFoldEvent(game, table, handState,
                            seat.getUserGameStatus().getUser().getUsername());
                }
            } else {
                currentCount = currentCount - 1;
                seatMap.put(seat, currentCount);
            }
        }
    }

}
