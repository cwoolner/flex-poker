package com.flexpoker.dao.mock;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.flexpoker.dao.GameDao;
import com.flexpoker.model.Game;

@Repository
public class GameDaoMock implements GameDao {

    @Override
    public List<Game> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Game findById(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(Game entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Integer id, Game entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        // TODO Auto-generated method stub

    }

}
