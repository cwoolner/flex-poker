package com.flexpoker.misc.mock;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    @SuppressWarnings("unchecked")
    @Override
    public EntityManager createEntityManager(Map arg0) {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

}
