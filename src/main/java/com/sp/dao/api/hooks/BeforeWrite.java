package com.sp.dao.api.hooks;

/**
 * Created by pankajmishra on 24/08/16.
 * Any class implementing this will be called before any write happens in database.
 */
public interface BeforeWrite<T, O extends Comparable>{
    boolean proceed(T spObject);
    O rank();
}
