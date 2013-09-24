package com.flexpoker.test.util.datageneration;

import java.util.Arrays;
import java.util.UUID;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class GameGenerator {

    public Game get(Game game, UUID[] uuidArray) {
        Table table1 = new Table();
        table1.setId(uuidArray[0]);
        RealTimeHand realTimeHand1 = new RealTimeHand(Arrays.asList(new Seat[]{new Seat()}));
        realTimeHand1.setHandDealerState(HandDealerState.NONE);

        Table table2 = new Table();
        table2.setId(uuidArray[1]);
        RealTimeHand realTimeHand2 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand2.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);

        Table table3 = new Table();
        table3.setId(uuidArray[2]);
        RealTimeHand realTimeHand3 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand3.setHandDealerState(HandDealerState.FLOP_DEALT);

        Table table4 = new Table();
        table4.setId(uuidArray[3]);
        RealTimeHand realTimeHand4 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand4.setHandDealerState(HandDealerState.TURN_DEALT);

        Table table5 = new Table();
        table5.setId(uuidArray[4]);
        RealTimeHand realTimeHand5 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand5.setHandDealerState(HandDealerState.RIVER_DEALT);

        Table table6 = new Table();
        table6.setId(uuidArray[5]);
        RealTimeHand realTimeHand6 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand6.setHandDealerState(HandDealerState.COMPLETE);

        game.addRealTimeHand(table1, realTimeHand1);
        game.addRealTimeHand(table2, realTimeHand2);
        game.addRealTimeHand(table3, realTimeHand3);
        game.addRealTimeHand(table4, realTimeHand4);
        game.addRealTimeHand(table5, realTimeHand5);
        game.addRealTimeHand(table6, realTimeHand6);

        return game;
    }

}
