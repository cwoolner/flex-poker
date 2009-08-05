package com.flexpoker.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameEventType;

@Repository("gameEventTypeDao")
public class GameEventTypeDaoImpl extends GenericDaoImpl<GameEventType, Integer>
        implements GameEventTypeDao {

    @Override
    public GameEventType findByName(String name) {
        Query query = entityManager.createQuery("from " + GameEventType.class.getName()
            + " where name = :name");
        query.setParameter("name", name);
        return (GameEventType) query.getSingleResult();
    }

}
