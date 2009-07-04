package com.flexpoker.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    /**
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param id
     * @param entity
     * @return
     */
    void save(ID id, T entity);

    /**
     * @param entity
     */
    void remove(T entity);

}
