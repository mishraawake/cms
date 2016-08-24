package com.sp.dao.jcr.hooks;

import com.sp.dao.api.ItemDao;
import com.sp.dao.api.hooks.BeforeWrite;
import com.sp.dao.api.hooks.CallPoint;
import com.sp.dao.api.hooks.ClassPoint;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.dao.jcr.validation.ValidatorProviderImpl;
import com.sp.model.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by pankajmishra on 24/08/16.
 */
@CallPoint(classNames = {@ClassPoint(target = "ItemDao" , methodNames = {})})
@Service
public class ItemValidationHook implements BeforeWrite<JCRItem, Integer> {

    @Autowired
    ValidatorProviderImpl validatorProvider;

    @Override
    public boolean proceed(JCRItem spObject) {
        Map<Field, String> errors = new HashMap<>();
        try {
            boolean result = spObject.getFieldValues().stream().
                    map((sp) -> validatorProvider.apply(sp.getField().getValueType()).
                            validate(sp, errors)).reduce(true, (b1, b2) -> {
                return b1 & b2;
            });
            return result;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer rank() {
        return 10;
    }
}
