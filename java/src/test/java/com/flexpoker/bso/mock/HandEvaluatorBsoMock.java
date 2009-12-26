package com.flexpoker.bso.mock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.HandEvaluatorBso;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.User;

@Service
public class HandEvaluatorBsoMock implements HandEvaluatorBso {

    @Override
    public List<HandRanking> determinePossibleHands(CommonCards commonCards) {
        return null;
    }

    @Override
    public HandEvaluation determineHandEvaluation(CommonCards commonCards, User user,
            PocketCards pocketCards, List<HandRanking> possibleHandRankings) {
        return null;
    }

}
