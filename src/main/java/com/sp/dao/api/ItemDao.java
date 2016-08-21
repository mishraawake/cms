package com.sp.dao.api;

import com.sp.model.IDefinition;
import com.sp.model.IItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface ItemDao<T extends IItem> extends IDao<T> {

    /**
     * @param toBeCreated
     * @param parentItem
     * @return
     * @throws DatabaseException
     */
    public T createChild(T toBeCreated, T parentItem) throws DatabaseException;

    /**
     * Items present at root.
     *
     * @return
     * @throws DatabaseException
     */
    public List<T> getRootItems() throws DatabaseException;

    /**
     * Get all associations.
     *
     * @param id
     * @return
     * @throws DatabaseException
     */
    public T getItemWithAssociations(Serializable id) throws DatabaseException;

    /**
     * get association with given name.
     *
     * @param id
     * @param name
     * @return
     * @throws DatabaseException
     */
    public T getItemWithAssociations(Serializable id, String name) throws DatabaseException;


    /**
     * Get all children of given node.
     *
     * @param id
     * @return
     */
    public List<T> getChildItems(Serializable id) throws DatabaseException;

    /**
     * @param item
     * @param <R>
     * @return
     * @throws DatabaseException
     */
    <R extends IDefinition> R getDefinition(T item) throws DatabaseException;
}
