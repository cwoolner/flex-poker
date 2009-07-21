package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Seat;

@Repository("seatDao")
public class SeatDaoImpl extends GenericDaoImpl<Seat, Integer> implements SeatDao {

}
