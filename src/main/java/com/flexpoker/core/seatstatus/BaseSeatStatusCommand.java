package com.flexpoker.core.seatstatus;

import java.util.List;
import java.util.Random;
import java.util.Timer;

import com.flexpoker.core.api.scheduling.ScheduleAndReturnActionOnTimerCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public abstract class BaseSeatStatusCommand {
    
    protected ScheduleAndReturnActionOnTimerCommand createAndStartActionOnTimerCommand;

    protected void assignNewHandActionOn(Game game, Table table) {
        List<Seat> seats = table.getSeats();
        Seat bigBlindSeat = table.getBigBlindSeat();
        Seat actionOnSeat = table.getActionOnSeat();
        int bigBlindIndex = seats.indexOf(bigBlindSeat);

        for (int i = bigBlindIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                if (actionOnSeat != null) {
                    actionOnSeat.setActionOn(false);
                }
                Seat newActionOnSeat = seats.get(i);
                newActionOnSeat.setActionOn(true);
                Timer actionOnTimer = createAndStartActionOnTimerCommand.execute(
                        game, table, newActionOnSeat);
                newActionOnSeat.setActionOnTimer(actionOnTimer);
                return;
            }
        }

        for (int i = 0; i < bigBlindIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                if (actionOnSeat != null) {
                    actionOnSeat.setActionOn(false);
                }
                Seat newActionOnSeat = seats.get(i);
                newActionOnSeat.setActionOn(true);
                Timer actionOnTimer = createAndStartActionOnTimerCommand.execute(
                        game, table, newActionOnSeat);
                newActionOnSeat.setActionOnTimer(actionOnTimer);
                return;
            }
        }
    }

    protected void assignNewGameBigBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = table.getNumberOfPlayers();
        Seat buttonSeat = table.getButtonSeat();
        Seat smallBlindSeat = table.getSmallBlindSeat();

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

    protected void assignNewGameSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();
        int numberOfPlayers = table.getNumberOfPlayers();
        Seat buttonSeat = table.getButtonSeat();

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

    protected void assignNewGameButton(Table table) {
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

    protected void assignNewRoundActionOn(Game game, Table table) {
        List<Seat> seats = table.getSeats();

        Seat buttonSeat = table.getButtonSeat();
        Seat actionOnSeat = table.getActionOnSeat();

        int buttonIndex = seats.indexOf(buttonSeat);

        for (int i = buttonIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                actionOnSeat.setActionOn(false);
                Seat newActionOnSeat = seats.get(i);
                newActionOnSeat.setActionOn(true);
                Timer actionOnTimer = createAndStartActionOnTimerCommand.execute(
                        game, table, newActionOnSeat);
                newActionOnSeat.setActionOnTimer(actionOnTimer);
                return;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            if (seats.get(i).isStillInHand()
                    && !seats.get(i).isAllIn()) {
                actionOnSeat.setActionOn(false);
                Seat newActionOnSeat = seats.get(i);
                newActionOnSeat.setActionOn(true);
                Timer actionOnTimer = createAndStartActionOnTimerCommand.execute(
                        game, table, newActionOnSeat);
                newActionOnSeat.setActionOnTimer(actionOnTimer);
                return;
            }
        }

    }

    protected void assignNewHandBigBlind(Table table) {
        Seat bigBlindSeat = table.getBigBlindSeat();
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

    protected void assignNewHandSmallBlind(Table table) {
        List<Seat> seats = table.getSeats();
        Seat smallBlindSeat = table.getSmallBlindSeat();
        Seat bigBlindSeat = table.getBigBlindSeat();

        // if only two people are left, switch to the heads-up rules.  just loop
        // through the table and find the first seat that is not the big blind.
        if (table.getNumberOfPlayers() == 2) {
            for (Seat seat : seats) {
                if (!bigBlindSeat.equals(seat) && seat.isStillInHand()) {
                    smallBlindSeat.setSmallBlind(false);
                    seat.setSmallBlind(true);
                    return;
                }
            }
        }

        smallBlindSeat = table.getSmallBlindSeat();
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

    protected void assignNewHandButton(Table table) {
        Seat buttonSeat = table.getButtonSeat();
        Seat smallBlindSeat = table.getSmallBlindSeat();
        if (table.getNumberOfPlayers() == 2) {
            buttonSeat.setButton(false);
            smallBlindSeat.setButton(true);
            return;
        }

        List<Seat> seats = table.getSeats();
        buttonSeat = table.getButtonSeat();
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

}
