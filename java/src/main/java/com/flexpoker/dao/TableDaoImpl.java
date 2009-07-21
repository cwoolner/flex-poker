package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Table;

@Repository("tableDao")
public class TableDaoImpl extends GenericDaoImpl<Table, Integer> implements TableDao {

}
