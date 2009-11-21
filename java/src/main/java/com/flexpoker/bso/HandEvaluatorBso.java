package com.flexpoker.bso;

import java.util.List;

import com.flexpoker.model.CommonCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.User;

public interface HandEvaluatorBso {

    List<HandRanking> determinePossibleHands(CommonCards commonCards);

    HandEvaluation determineHandEvaluation(CommonCards commonCards, User user,
            PocketCards pocketCards, List<HandRanking> possibleHandRankings);

}
