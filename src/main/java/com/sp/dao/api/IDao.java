package com.sp.dao.api;

import com.sp.dao.api.DatabaseException;

import java.io.Serializable;

/**
 * Created by pankajmishra on 06/08/16.
 * Generic dao. It should offer all the crud operation for T.
 */
public interface IDao<T> {
    T create(T element) throws DatabaseException;
    T createOrUpdate(T element) throws DatabaseException;
    T update(T element) throws DatabaseException;
    T get(Serializable id) throws DatabaseException;
    Iterable<T> list() throws DatabaseException;
    Long count() throws DatabaseException;
    void delete(Serializable id) throws DatabaseException;
    boolean exists(Serializable id) throws DatabaseException;
}
