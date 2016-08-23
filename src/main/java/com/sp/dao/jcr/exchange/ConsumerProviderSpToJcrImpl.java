package com.sp.dao.jcr.exchange;

import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.dao.api.exchange.SpToJcr;
import com.sp.model.ValueType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service
public class ConsumerProviderSpToJcrImpl<T> implements ConsumerProviderSpToJcr<T>, ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public SpToJcr<T> apply(ValueType t) {
        return applicationContext.getBean(SpToJcr.class.getSimpleName() + t.name(), SpToJcr.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
