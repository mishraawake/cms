package com.sp.dao.api;

import com.sp.model.IDefinition;

import java.io.Serializable;

/**
 * Created by pankajmishra on 06/08/16.
 */
public interface DefinitionDao<T extends IDefinition> extends IDao<T> {

    T create(T element) throws DatabaseException;

    T createOrUpdate(T element) throws DatabaseException;

    T update(T element) throws DatabaseException;

    T get(Serializable id) throws DatabaseException;

    Iterable<T> list() throws DatabaseException;

    Long count() throws DatabaseException;

    void delete(Serializable id) throws DatabaseException;

    boolean exists(Serializable id) throws DatabaseException;


}
