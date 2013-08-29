package com.flexpoker.repository;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.flexpoker.model.Game;
import com.flexpoker.repository.api.GameRepository;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final SessionFactory sessionFactory;
    
    @Inject
    public GameRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Game findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (Game) session.get(Game.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Game> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Game.class).list();
    }

    @Override
    public void save(Game entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
    }
}
