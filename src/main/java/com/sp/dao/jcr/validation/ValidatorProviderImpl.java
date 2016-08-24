package com.sp.dao.jcr.validation;

import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.api.validation.Validator;
import com.sp.dao.api.validation.ValidatorProvider;
import com.sp.model.FieldValue;
import com.sp.model.ValueType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service
public class ValidatorProviderImpl implements ValidatorProvider<FieldValue>, ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public Validator<FieldValue> apply(ValueType valueType) {
        if(applicationContext.containsBean (valueType.name() + "Validator")){
           return applicationContext.getBean (valueType.name() + "Validator", Validator.class);
        }
        if(valueType.isStringType()){
            if(valueType.isArray()){
                return applicationContext.getBean ("ArrayOfStringValidator", Validator.class);
            } else {
                return applicationContext.getBean ("StringValidator", Validator.class);
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
