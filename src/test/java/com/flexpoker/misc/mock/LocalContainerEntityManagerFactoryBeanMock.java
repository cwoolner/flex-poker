package com.flexpoker.misc.mock;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

/**
 * Mock class being used so that testApplicationContext.xml has an
 * EntityManagerFactory defined.  None of these methods are meant to be
 * implemented.
 *
 * @author cwoolner
 */
public class LocalContainerEntityManagerFactoryBeanMock implements EntityManagerFactory {

    @Override
    public void close() {}

    @Override
    public EntityManager createEntityManager() {
        return null;
    }

    @Override
    public EntityManager createEntityManager(Map arg0) {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public Cache getCache() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

}
