package com.flexpoker.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.flexpoker.model.GameEvent;
import com.flexpoker.model.Table;
import com.flexpoker.util.IntegrationContext;
import com.flexpoker.util.IntegrationTest;

public class GameEventDaoImplIntTest extends IntegrationTest {

    private GameEventDao gameEventDao = (GameEventDao) IntegrationContext.instance().getBean("gameEventDao");

    private TableDao tableDao = (TableDao) IntegrationContext.instance().getBean("tableDao");
    @Test
    public void testFindLatestGameEvent() throws Exception {
        setEntityManagers(gameEventDao, tableDao);

        Table table = new Table();

        entityTransaction.begin();
        tableDao.save(table.getId(), table);
        entityTransaction.commit();

        GameEvent gameEvent1 = new GameEvent();
        GameEvent gameEvent2 = new GameEvent();
        GameEvent gameEvent3 = new GameEvent();
        gameEvent1.setTable(table);
        gameEvent2.setTable(table);
        gameEvent3.setTable(table);

        gameEvent1.setEventTime(new Date());
        Thread.currentThread().sleep(1000L);
        gameEvent3.setEventTime(new Date());
        Thread.currentThread().sleep(1000L);
        gameEvent2.setEventTime(new Date());

        entityTransaction.begin();
        gameEventDao.save(gameEvent1.getId(), gameEvent1);
        gameEventDao.save(gameEvent2.getId(), gameEvent2);
        gameEventDao.save(gameEvent3.getId(), gameEvent3);
        entityTransaction.commit();

        assertEquals(gameEvent2, gameEventDao.findLatestTableEvent(table));

        entityTransaction.begin();
        gameEventDao.remove(gameEvent1);
        gameEventDao.remove(gameEvent2);
        gameEventDao.remove(gameEvent3);
        tableDao.remove(table);
        entityTransaction.commit();
    }

}
