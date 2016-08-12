package com.sp.dao.api;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.IDao;
import com.sp.model.IDefinition;
import com.sp.model.IItem;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface ItemDao<T extends IItem> extends IDao<T> {

    /**
     * This method is introduced in itemdao to create an item when some item which it would
     * linked to has already been created. This is particularly desired because we need to know
     * on which location in the complex graph of cms this particular item is being created.
     * @param toBeCreated
     * @param alreadyCreated
     * @param linkName
     * @return
     */
    T createWithLink(T toBeCreated, T alreadyCreated, String linkName ) throws DatabaseException;


    <R extends IDefinition> R getDefinition(T item) throws DatabaseException;
}
