package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.UserGameStatus;

@Repository("userGameStatusDao")
public class UserGameStatusDaoImpl extends GenericDaoImpl<UserGameStatus, Integer>
        implements UserGameStatusDao {

}
