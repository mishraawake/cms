package com.sp.service.impl;

import com.sp.model.IUser;
import com.sp.service.RunTimeContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankajmishra on 18/08/16.
 */
@Service
public class MapContext<T> implements RunTimeContext<T> {

    private Map<String, T> map = new HashMap<>();

    @Override
    public void setValue(String key, T value) {
        map.put(key, value);
    }

    @Override
    public T getValue(String key) {
        return map.get(key);
    }
}
