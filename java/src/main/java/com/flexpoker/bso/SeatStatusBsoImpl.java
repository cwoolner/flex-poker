package com.flexpoker.bso;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service("seatStatusBso")
public class SeatStatusBsoImpl implements SeatStatusBso {

    private ValidationBso validationBso;

    @Override
    public void setStatusForNewGame(Table table) {
        validationBso.validateTable(table);
        assignStillInHand(table);
        assignNewGameButton(table);
        assignNewGameSmallBlind(table);
        assignNewGameBigBlind(table);
        assignNewGameActionOn(table);
    }
    
    @Override
    public void setStatusForNewRound(Table table) {
        validationBso.validateTable(table);
        assignNewRoundActionOn(table);
    }

    @Override
    public void setStatusForNewHand(Table table) {
        validationBso.validateTable(table);
        assignStillInHand(table);
    }

    private void assignStillInHand(Table table) {
        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null) {
                seat.setStillInHand(true);
            }
        }
    }

    private void assignNewGameActionOn(Table table) {
        // TODO: Implement for real.
        table.setActionOn(table.getSeats().get(1));
    }

    private void assignNewGameBigBlind(Table table) {
        // TODO: Implement for real.
        table.setBigBlind(table.getSeats().get(0));
    }

    private void assignNewGameSmallBlind(Table table) {
        // TODO: Implement for real.
        table.setSmallBlind(table.getSeats().get(1));
    }

    private void assignNewGameButton(Table table) {
        int numberOfPlayersAtTable = table.getSeats().size();
        while (true) {
            int dealerPosition = new Random().nextInt(numberOfPlayersAtTable);
            Seat seat = table.getSeats().get(dealerPosition);
            if (seat.getUserGameStatus() != null) {
                table.setButton(table.getSeats().get(dealerPosition));
                break;
            }
        }
    }

    private void assignNewRoundActionOn(Table table) {
        List<Seat> seats = table.getSeats();
        int buttonIndex = seats.indexOf(table.getButton());

        for (int i = buttonIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }

    }

    public ValidationBso getValidationBso() {
        return validationBso;
    }

    public void setValidationBso(ValidationBso validationBso) {
        this.validationBso = validationBso;
    }

}
