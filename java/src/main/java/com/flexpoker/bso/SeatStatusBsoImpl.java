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
        assignNewHandActionOn(table);
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
        assignNewHandBigBlind(table);
        assignNewHandSmallBlind(table);
        assignNewHandButton(table);
        assignNewHandActionOn(table);
    }

    private void assignStillInHand(Table table) {
        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null) {
                seat.setStillInHand(true);
            }
        }
    }

    private void assignNewHandActionOn(Table table) {
        List<Seat> seats = table.getSeats();
        int bigBlindIndex = seats.indexOf(table.getBigBlind());

        for (int i = bigBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < bigBlindIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }
    }

    private void assignNewGameBigBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = determineNumberOfPlayers(table);

        if (numberOfPlayers == 2) {
            int buttonIndex = seats.indexOf(table.getButton());

            for (int i = buttonIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setBigBlind(seats.get(i));
                    return;
                }
            }

            for (int i = 0; i < buttonIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setBigBlind(seats.get(i));
                    return;
                }
            }
        } else {
            int smallBlindIndex = seats.indexOf(table.getSmallBlind());

            for (int i = smallBlindIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setBigBlind(seats.get(i));
                    return;
                }
            }

            for (int i = 0; i < smallBlindIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setBigBlind(seats.get(i));
                    return;
                }
            }
        }
    }

    private void assignNewGameSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = determineNumberOfPlayers(table);

        if (numberOfPlayers == 2) {
            table.setSmallBlind(table.getButton());
        } else {
            int buttonIndex = seats.indexOf(table.getButton());

            for (int i = buttonIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setSmallBlind(seats.get(i));
                    return;
                }
            }

            for (int i = 0; i < buttonIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    table.setSmallBlind(seats.get(i));
                    return;
                }
            }
        }
    }

    private void assignNewGameButton(Table table) {
        int numberOfPlayersAtTable = table.getSeats().size();
        while (true) {
            int dealerPosition = new Random().nextInt(numberOfPlayersAtTable);
            Seat seat = table.getSeats().get(dealerPosition);
            if (seat.isStillInHand()) {
                table.setButton(table.getSeats().get(dealerPosition));
                break;
            }
        }
    }

    private void assignNewRoundActionOn(Table table) {
        List<Seat> seats = table.getSeats();
        int buttonIndex = seats.indexOf(table.getButton());

        for (int i = buttonIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                table.setActionOn(seats.get(i));
                return;
            }
        }

    }

    private Integer determineNumberOfPlayers(Table table) {
        int numberOfPlayers = 0;

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null) {
                numberOfPlayers++;
            }
        }

        return numberOfPlayers;
    }

    private void assignNewHandBigBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int bigBlindIndex = seats.indexOf(table.getBigBlind());

        for (int i = bigBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                table.setBigBlind(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < bigBlindIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                table.setBigBlind(seats.get(i));
                return;
            }
        }
    }

    private void assignNewHandSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();

        // if only two people are left, switch to the heads-up rules.  just loop
        // through the table and find the first seat that is not the big blind.
        if (determineNumberOfPlayers(table) == 2) {
            for (Seat seat : seats) {
                if (!table.getBigBlind().equals(seat) && seat.isStillInHand()) {
                    table.setSmallBlind(seat);
                    return;
                }
            }
        }

        int smallBlindIndex = seats.indexOf(table.getSmallBlind());

        for (int i = smallBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                table.setSmallBlind(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < smallBlindIndex; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                table.setSmallBlind(seats.get(i));
                return;
            }
        }

    }

    private void assignNewHandButton(Table table) {
        if (determineNumberOfPlayers(table) == 2) {
            table.setButton(table.getSmallBlind());
            return;
        }

        List<Seat> seats = table.getSeats();
        int buttonIndex = seats.indexOf(table.getButton());

        for (int i = buttonIndex + 1; i < seats.size() ; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                table.setButton(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                table.setButton(seats.get(i));
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
