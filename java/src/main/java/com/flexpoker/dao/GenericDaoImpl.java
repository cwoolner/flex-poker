package com.flexpoker.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    protected EntityManager entityManager;
    
    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>) entityManager.createQuery("from "
                + persistentClass.getName()).getResultList();
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(persistentClass, id);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void save(ID id, T entity) {
        if (id == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
