package com.flexpoker.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.HandEvaluatorBso;
import com.flexpoker.bso.api.PotBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
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
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.MessagingConstants;
import com.flexpoker.util.SmallBlindSeatPredicate;
import com.flexpoker.web.model.PocketCardsViewModel;

@Command
public class InitializeAndStartGameImplCommand implements InitializeAndStartGameCommand {

    private final AssignInitialTablesForNewGame assignInitialTablesForNewGame;
    
    private final SimpMessageSendingOperations messagingTemplate;
    
    private final SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand;
    
    private final GameRepository gameRepository;
    
    private final DeckBso deckBso;

    private final PotBso potBso;
    
    private final HandEvaluatorBso handEvaluatorBso;
    
    @Inject
    public InitializeAndStartGameImplCommand(
            AssignInitialTablesForNewGame assignInitialTablesForNewGame,
            SimpMessageSendingOperations messagingTemplate,
            SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand,
            GameRepository gameRepository,
            DeckBso deckBso,
            PotBso potBso,
            HandEvaluatorBso handEvaluatorBso) {
        this.assignInitialTablesForNewGame = assignInitialTablesForNewGame;
        this.messagingTemplate = messagingTemplate;
        this.setSeatStatusForNewGameCommand = setSeatStatusForNewGameCommand;
        this.gameRepository = gameRepository;
        this.deckBso = deckBso;
        this.potBso = potBso;
        this.handEvaluatorBso = handEvaluatorBso;
    }
    
    @Override
    public void execute(final UUID gameId) {
        assignInitialTablesForNewGame.execute(gameId);
        
        final Game game = gameRepository.findById(gameId);

        Set<UserGameStatus> userGameStatuses = game.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatuses) {
            userGameStatus.setChips(1500);
        }
        
        for (Table table : game.getTables()) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null) {
                    messagingTemplate.convertAndSendToUser(
                            seat.getUserGameStatus().getUser().getUsername(),
                            MessagingConstants.OPEN_TABLE_FOR_USER,
                            new OpenTableForUserDto(gameId, table.getId()));
                }
            }
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Table table: game.getTables()) {
                    setSeatStatusForNewGameCommand.execute(table);
                    Game game = gameRepository.findById(gameId);
                    resetTableStatus(game, table);
                    messagingTemplate.convertAndSend(String.format(
                            MessagingConstants.TABLE_STATUS, gameId, table.getId()),
                            table);
                }
                timer.cancel();
            }
        }, 5000);
    }
    
    private void resetTableStatus(Game game, Table table) {
        table.setTotalPotAmount(0);
        deckBso.shuffleDeck(game, table);
        potBso.createNewHandPot(game, table);
        createNewRealTimeHand(game, table);
        determineTablePotAmounts(game, table);
    }
    
    private void createNewRealTimeHand(Game game, Table table) {
        Blinds currentBlinds = game.getCurrentBlinds();
        int smallBlind = currentBlinds.getSmallBlind();
        int bigBlind = currentBlinds.getBigBlind();

        RealTimeHand realTimeHand = new RealTimeHand(table.getSeats());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() == null) {
                continue;
            }

            int chipsInFront = 0;
            int callAmount = bigBlind;
            int raiseToAmount = bigBlind * 2;

            if (seat.equals(bigBlindSeat)) {
                chipsInFront = bigBlind;
                callAmount = 0;
            } else if (seat.equals(smallBlindSeat)) {
                chipsInFront = smallBlind;
                callAmount = smallBlind;
            }

            if (chipsInFront > seat.getUserGameStatus().getChips()) {
                seat.setChipsInFront(seat.getUserGameStatus().getChips());
            } else {
                seat.setChipsInFront(chipsInFront);
            }

            seat.getUserGameStatus().setChips(
                    seat.getUserGameStatus().getChips() - seat.getChipsInFront());

            if (callAmount > seat.getUserGameStatus().getChips()) {
                seat.setCallAmount(seat.getUserGameStatus().getChips());
            } else {
                seat.setCallAmount(callAmount);
            }

            int totalChips = seat.getUserGameStatus().getChips()
                    + seat.getChipsInFront();

            if (raiseToAmount > totalChips) {
                seat.setRaiseTo(totalChips);
            } else {
                seat.setRaiseTo(raiseToAmount);
            }

            table.setTotalPotAmount(table.getTotalPotAmount()
                    + seat.getChipsInFront());

            if (seat.getRaiseTo() > 0) {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.RAISE);
            }

            if (seat.getCallAmount() > 0) {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CALL);
                realTimeHand.addPossibleSeatAction(seat, GameEventType.FOLD);
            } else {
                realTimeHand.addPossibleSeatAction(seat, GameEventType.CHECK);
            }
        }

        determineNextToAct(table, realTimeHand);
        realTimeHand.setLastToAct(bigBlindSeat);

        realTimeHand.setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);
        realTimeHand.setHandRoundState(HandRoundState.ROUND_IN_PROGRESS);

        List<HandEvaluation> handEvaluations = determineHandEvaluations(game, table);
        realTimeHand.setHandEvaluationList(handEvaluations);

        game.addRealTimeHand(table, realTimeHand);
    }

    private List<HandEvaluation> determineHandEvaluations(Game game, Table table) {
        FlopCards flopCards = deckBso.fetchFlopCards(game, table);
        TurnCard turnCard = deckBso.fetchTurnCard(game, table);
        RiverCard riverCard = deckBso.fetchRiverCard(game, table);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHands = handEvaluatorBso.determinePossibleHands(commonCards);

        List<HandEvaluation> handEvaluations = new ArrayList<>();

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null
                    && seat.getUserGameStatus().getUser() != null) {
                User user = seat.getUserGameStatus().getUser();
                PocketCards pocketCards = deckBso.fetchPocketCards(user, game, table);
                HandEvaluation handEvaluation = handEvaluatorBso
                        .determineHandEvaluation(commonCards, user, pocketCards,
                         possibleHands);
                handEvaluations.add(handEvaluation);
                
                PocketCardsViewModel pocketCardsViewModel = new PocketCardsViewModel(
                        pocketCards.getCard1().getId(),
                        pocketCards.getCard2().getId(),
                        table.getId());
                messagingTemplate.convertAndSendToUser(user.getUsername(), MessagingConstants.POCKET_CARDS, pocketCardsViewModel);
            }
        }

        return handEvaluations;
    }

    private void determineNextToAct(Table table, RealTimeHand realTimeHand) {
        List<Seat> seats = table.getSeats();
        Seat actionOnSeat = (Seat) CollectionUtils.find(seats,
                new ActionOnSeatPredicate());

        int actionOnIndex = seats.indexOf(actionOnSeat);

        for (int i = actionOnIndex + 1; i < seats.size(); i++) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setNextToAct(seats.get(i));
                return;
            }
        }

        for (int i = 0; i < actionOnIndex; i++) {
            if (seats.get(i).isStillInHand()) {
                realTimeHand.setNextToAct(seats.get(i));
                return;
            }
        }
    }
    
    private void determineTablePotAmounts(Game game, Table table) {
        table.setPotAmounts(new ArrayList<Integer>());
        for (Pot pot : potBso.fetchAllPots(game, table)) {
            table.getPotAmounts().add(pot.getAmount());
        }
    }

    private class OpenTableForUserDto {

        private final UUID gameId;
        
        private final UUID tableId;
        
        public OpenTableForUserDto(UUID gameId, UUID tableId) {
            this.gameId = gameId;
            this.tableId = tableId;
        }

        public UUID getGameId() {
            return gameId;
        }

        public UUID getTableId() {
            return tableId;
        }

    }

}
