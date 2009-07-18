package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.UserStatusInGame;

@Repository("userStatusInGameDao")
public class UserStatusInGameDaoImpl extends GenericDaoImpl<UserStatusInGame, Integer>
        implements UserStatusInGameDao {

}
