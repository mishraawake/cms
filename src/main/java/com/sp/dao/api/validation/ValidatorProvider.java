package com.sp.dao.api.validation;

import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.dao.api.exchange.SpToJcr;
import com.sp.model.ValueType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service
public interface ValidatorProvider<T> extends Function<ValueType, Validator<T>> {
    @Override
    Validator<T> apply(ValueType t);
}
