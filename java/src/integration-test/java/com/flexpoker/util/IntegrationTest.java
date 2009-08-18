package com.flexpoker.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.flexpoker.dao.GenericDao;

public class IntegrationTest {

    private EntityManagerFactory entityManagerFactory = (EntityManagerFactory)
            IntegrationContext.instance().getBean("entityManagerFactory");

    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    protected EntityTransaction entityTransaction = entityManager.getTransaction();

    protected void setEntityManagers(GenericDao<?, ?>... daos) {
        for (GenericDao<?, ?> dao : daos) {
            dao.setEntityManager(entityManager);
        }
    }

}
