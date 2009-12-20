package com.flexpoker.bso;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.SmallBlindSeatPredicate;

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
        resetChipsInFront(table);
        assignNewRoundActionOn(table);
    }

    @Override
    public void setStatusForNewHand(Table table) {
        validationBso.validateTable(table);
        assignStillInHand(table);
        resetShowCards(table);
        assignNewHandBigBlind(table);
        assignNewHandSmallBlind(table);
        assignNewHandButton(table);
        assignNewHandActionOn(table);
    }

    @Override
    public void setStatusForEndOfHand(Table table) {
        resetChipsInFront(table);
    }

    /**
     * Zero-out the chipsInFront field for all seats at a table.  This should
     * typically be used whenever a new round is started.
     *
     * @param table
     */
    private void resetChipsInFront(Table table) {
        for (Seat seat : table.getSeats()) {
            seat.setChipsInFront(0);
        }
    }

    private void resetShowCards(Table table) {
        for (Seat seat : table.getSeats()) {
            seat.setShowCards(null);
        }
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
        Seat bigBlindSeat = (Seat) CollectionUtils.find(seats,
                new BigBlindSeatPredicate());
        Seat actionOnSeat = (Seat) CollectionUtils.find(seats,
                new ActionOnSeatPredicate());
        int bigBlindIndex = seats.indexOf(bigBlindSeat);

        for (int i = bigBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                if (actionOnSeat != null) {
                    actionOnSeat.setActionOn(false);
                }
                seats.get(i).setActionOn(true);
                return;
            }
        }

        for (int i = 0; i < bigBlindIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                if (actionOnSeat != null) {
                    actionOnSeat.setActionOn(false);
                }
                seats.get(i).setActionOn(true);
                return;
            }
        }
    }

    private void assignNewGameBigBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = determineNumberOfPlayers(table);
        Seat buttonSeat = (Seat) CollectionUtils.find(seats,
                new ButtonSeatPredicate());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(seats,
                new SmallBlindSeatPredicate());

        if (numberOfPlayers == 2) {
            int buttonIndex = seats.indexOf(buttonSeat);

            for (int i = buttonIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setBigBlind(true);
                    return;
                }
            }

            for (int i = 0; i < buttonIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setBigBlind(true);
                    return;
                }
            }
        } else {
            int smallBlindIndex = seats.indexOf(smallBlindSeat);

            for (int i = smallBlindIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setBigBlind(true);
                    return;
                }
            }

            for (int i = 0; i < smallBlindIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setBigBlind(true);
                    return;
                }
            }
        }
    }

    private void assignNewGameSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = determineNumberOfPlayers(table);
        Seat buttonSeat = (Seat) CollectionUtils.find(seats,
                new ButtonSeatPredicate());

        if (numberOfPlayers == 2) {
            buttonSeat.setSmallBlind(true);
        } else {
            int buttonIndex = seats.indexOf(buttonSeat);

            for (int i = buttonIndex + 1; i < seats.size(); i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setSmallBlind(true);
                    return;
                }
            }

            for (int i = 0; i < buttonIndex; i++) {
                if (seats.get(i).isStillInHand()) {
                    seats.get(i).setSmallBlind(true);
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
                table.getSeats().get(dealerPosition).setButton(true);
                break;
            }
        }
    }

    private void assignNewRoundActionOn(Table table) {
        List<Seat> seats = table.getSeats();

        Seat buttonSeat = (Seat) CollectionUtils.find(seats,
                new ButtonSeatPredicate());
        Seat actionOnSeat = (Seat) CollectionUtils.find(seats,
                new ActionOnSeatPredicate());

        int buttonIndex = seats.indexOf(buttonSeat);

        for (int i = buttonIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                actionOnSeat.setActionOn(false);
                seats.get(i).setActionOn(true);
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                actionOnSeat.setActionOn(false);
                seats.get(i).setActionOn(true);
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
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
        List<Seat> seats = table.getSeats();
        int bigBlindIndex = seats.indexOf(bigBlindSeat);

        for (int i = bigBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                bigBlindSeat.setBigBlind(false);
                seats.get(i).setBigBlind(true);
                return;
            }
        }

        for (int i = 0; i < bigBlindIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                bigBlindSeat.setBigBlind(false);
                seats.get(i).setBigBlind(true);
                return;
            }
        }
    }

    private void assignNewHandSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();
        Seat smallBlindSeat = (Seat) CollectionUtils.find(seats,
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(seats,
                new BigBlindSeatPredicate());

        // if only two people are left, switch to the heads-up rules.  just loop
        // through the table and find the first seat that is not the big blind.
        if (determineNumberOfPlayers(table) == 2) {
            for (Seat seat : seats) {
                if (!bigBlindSeat.equals(seat) && seat.isStillInHand()) {
                    smallBlindSeat.setSmallBlind(false);
                    seat.setSmallBlind(true);
                    return;
                }
            }
        }

        smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        int smallBlindIndex = seats.indexOf(smallBlindSeat);

        for (int i = smallBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                smallBlindSeat.setSmallBlind(false);
                seats.get(i).setSmallBlind(true);
                return;
            }
        }

        for (int i = 0; i < smallBlindIndex; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                smallBlindSeat.setSmallBlind(false);
                seats.get(i).setSmallBlind(true);
                return;
            }
        }

    }

    private void assignNewHandButton(Table table) {
        Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        if (determineNumberOfPlayers(table) == 2) {
            buttonSeat.setButton(false);
            smallBlindSeat.setButton(true);
            return;
        }

        List<Seat> seats = table.getSeats();
        buttonSeat = (Seat) CollectionUtils.find(seats,
                new ButtonSeatPredicate());
        int buttonIndex = seats.indexOf(buttonSeat);

        for (int i = buttonIndex + 1; i < seats.size() ; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                buttonSeat.setButton(false);
                seats.get(i).setButton(true);
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand() || seats.get(i).isPlayerJustLeft()) {
                buttonSeat.setButton(false);
                seats.get(i).setButton(true);
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
