package com.sp.service;

/**
 * Created by pankajmishra on 18/08/16.
 */
public interface RunTimeContext<T> {
    public void setValue(String key, T value);
    public T getValue(String key);
}
