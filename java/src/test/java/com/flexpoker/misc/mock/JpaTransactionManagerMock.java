package com.flexpoker.misc.mock;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;

/**
 * Mock class being used so that testApplicationContext.xml has an
 * EntityManagerFactory defined.  None of these methods are meant to be
 * implemented.
 *
 * @author cwoolner
 */
public class JpaTransactionManagerMock implements ResourceTransactionManager {

    @Override
    public Object getResourceFactory() {
        return null;
    }

    @Override
    public void commit(TransactionStatus arg0) throws TransactionException {}

    @Override
    public TransactionStatus getTransaction(TransactionDefinition arg0) throws TransactionException {
        return null;
    }

    @Override
    public void rollback(TransactionStatus arg0) throws TransactionException {}

}
