package com.flexpoker.web.translator.table;

import com.flexpoker.model.Seat;
import com.flexpoker.web.model.table.SeatViewModel;

public class SeatTranslator {

    public SeatViewModel translate(Seat seat) {
        return new SeatViewModel(seat.getPosition(), seat.getUserGameStatus()
                .getUser().getUsername(), seat.getUserGameStatus().getChips(),
                seat.getChipsInFront(), seat.isStillInHand(),
                seat.getRaiseTo(), seat.getCallAmount(), seat.isButton(),
                seat.isSmallBlind(), seat.isBigBlind(), seat.isActionOn());
    }
}
