package com.flexpoker.test.util.datageneration;

import java.util.Arrays;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class RealTimeGameGenerator {

    public RealTimeGame get(Game game) {
        RealTimeGame realTimeGame = new RealTimeGame();

        Table table1 = new Table();
        table1.setId(1);
        RealTimeHand realTimeHand1 = new RealTimeHand(Arrays.asList(new Seat[]{new Seat()}));
        realTimeHand1.setHandDealerState(HandDealerState.NONE);

        Table table2 = new Table();
        table2.setId(2);
        RealTimeHand realTimeHand2 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand2.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);

        Table table3 = new Table();
        table3.setId(3);
        RealTimeHand realTimeHand3 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand3.setHandDealerState(HandDealerState.FLOP_DEALT);

        Table table4 = new Table();
        table4.setId(4);
        RealTimeHand realTimeHand4 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand4.setHandDealerState(HandDealerState.TURN_DEALT);

        Table table5 = new Table();
        table5.setId(5);
        RealTimeHand realTimeHand5 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand5.setHandDealerState(HandDealerState.RIVER_DEALT);

        Table table6 = new Table();
        table6.setId(6);
        RealTimeHand realTimeHand6 = new RealTimeHand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand6.setHandDealerState(HandDealerState.COMPLETE);

        realTimeGame.addRealTimeHand(table1, realTimeHand1);
        realTimeGame.addRealTimeHand(table2, realTimeHand2);
        realTimeGame.addRealTimeHand(table3, realTimeHand3);
        realTimeGame.addRealTimeHand(table4, realTimeHand4);
        realTimeGame.addRealTimeHand(table5, realTimeHand5);
        realTimeGame.addRealTimeHand(table6, realTimeHand6);

        return realTimeGame;
    }

}
