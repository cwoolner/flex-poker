package com.flexpoker.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameEvent;
import com.flexpoker.model.Table;

@Repository("gameEventDao")
public class GameEventDaoImpl extends GenericDaoImpl<GameEvent, Integer>
        implements GameEventDao {

    @Override
    public GameEvent findLatestTableEvent(Table table) {
        Query query = entityManager.createQuery("from " + persistentClass.getName()
                + " where table = :table order by eventTime desc");
        query.setParameter("table", table);
        query.setMaxResults(1);
        return (GameEvent) query.getSingleResult();
    }

}
