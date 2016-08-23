package com.sp.dao.api.exchange;

import com.sp.model.ValueType;

import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
public interface ConsumerProviderSpToJcr<T> extends Function<ValueType, SpToJcr<T>> {
     @Override
     SpToJcr<T> apply(ValueType t);
}
