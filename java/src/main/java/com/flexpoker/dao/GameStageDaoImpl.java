package com.flexpoker.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameStage;

@Repository("gameStageDao")
public class GameStageDaoImpl extends GenericDaoImpl<GameStage, Integer> implements GameStageDao {

    @Override
    public GameStage findByName(String name) {
        Query query = entityManager.createQuery("from " + GameStage.class.getName()
            + " where name = :name");
        query.setParameter("name", name);
        return (GameStage) query.getSingleResult();
    }

}
