package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.GameBso;
import com.flexpoker.bso.api.HandEvaluatorBso;
import com.flexpoker.bso.api.PotBso;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Pot;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.SmallBlindSeatPredicate;

@Service
public class GameEventBsoImpl {

    private final GameBso gameBso;


    private final RealTimeGameRepository realTimeGameBso;

    private final SetSeatStatusForNewHandCommand setSeatStatusForNewHandCommand;
    
    private final SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand;

    private final HandEvaluatorBso handEvaluatorBso;

    @Inject
    public GameEventBsoImpl(GameBso gameBso, DeckBso deckBso,
            RealTimeGameRepository realTimeGameRepository,
            SetSeatStatusForNewHandCommand setSeatStatusForNewHandCommand,
            SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand,
            HandEvaluatorBso handEvaluatorBso, PotBso potBso) {
        this.gameBso = gameBso;
        this.realTimeGameBso = realTimeGameRepository;
        this.setSeatStatusForNewHandCommand = setSeatStatusForNewHandCommand;
        this.setSeatStatusForNewGameCommand = setSeatStatusForNewGameCommand;
        this.handEvaluatorBso = handEvaluatorBso;
    }

    private void determineLastToAct(Table table, RealTimeHand realTimeHand) {
        List<Seat> seats = table.getSeats();

        int seatIndex;

        if (realTimeHand.getOriginatingBettor() == null) {
            Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                    new ButtonSeatPredicate());
            seatIndex = seats.indexOf(buttonSeat);
        } else {
            seatIndex = seats.indexOf(realTimeHand.getOriginatingBettor());
            if (seatIndex == 0) {
                seatIndex = seats.size() - 1;
            } else {
                seatIndex--;
            }
        }

        for (int i = seatIndex; i >= 0; i--) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }

        for (int i = seats.size() - 1; i > seatIndex; i--) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setLastToAct(seats.get(i));
                return;
            }
        }
    }


}
