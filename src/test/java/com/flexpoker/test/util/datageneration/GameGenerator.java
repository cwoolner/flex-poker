package com.flexpoker.test.util.datageneration;

import java.util.Arrays;
import java.util.UUID;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.Hand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class GameGenerator {

    public Game get(UUID[] uuidArray) {
        Table table1 = new Table();
        table1.setId(uuidArray[0]);
        Hand realTimeHand1 = new Hand(Arrays.asList(new Seat[]{new Seat()}));
        realTimeHand1.setHandDealerState(HandDealerState.NONE);

        Table table2 = new Table();
        table2.setId(uuidArray[1]);
        Hand realTimeHand2 = new Hand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand2.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);

        Table table3 = new Table();
        table3.setId(uuidArray[2]);
        Hand realTimeHand3 = new Hand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand3.setHandDealerState(HandDealerState.FLOP_DEALT);

        Table table4 = new Table();
        table4.setId(uuidArray[3]);
        Hand realTimeHand4 = new Hand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand4.setHandDealerState(HandDealerState.TURN_DEALT);

        Table table5 = new Table();
        table5.setId(uuidArray[4]);
        Hand realTimeHand5 = new Hand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand5.setHandDealerState(HandDealerState.RIVER_DEALT);

        Table table6 = new Table();
        table6.setId(uuidArray[5]);
        Hand realTimeHand6 = new Hand(Arrays.asList(
                new Seat[]{new Seat()}));
        realTimeHand6.setHandDealerState(HandDealerState.COMPLETE);

        table1.setCurrentHand(realTimeHand1);
        table2.setCurrentHand(realTimeHand2);
        table3.setCurrentHand(realTimeHand3);
        table4.setCurrentHand(realTimeHand4);
        table5.setCurrentHand(realTimeHand5);
        table6.setCurrentHand(realTimeHand6);
        
        Game game = new Game();
        game.setId(UUID.randomUUID());
        
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        game.addTable(table4);
        game.addTable(table5);
        game.addTable(table6);
        
        return game;
    }

}
