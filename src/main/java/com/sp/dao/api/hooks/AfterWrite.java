package com.sp.dao.api.hooks;

/**
 * Created by pankajmishra on 24/08/16.
 * Any class implementing this will be called after the write in database.
 */
public interface AfterWrite<T, O extends Comparable> {
    void doThis(T spObject);
    O rank();
}
