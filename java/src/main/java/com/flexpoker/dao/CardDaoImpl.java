package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Card;

@Repository("cardDao")
public class CardDaoImpl extends GenericDaoImpl<Card, Integer> implements CardDao {

}
