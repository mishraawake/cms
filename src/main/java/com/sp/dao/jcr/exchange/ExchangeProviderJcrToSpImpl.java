package com.sp.dao.jcr.exchange;

import com.sp.dao.api.exchange.ExchangeProviderJcrToSp;
import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service
public class ExchangeProviderJcrToSpImpl implements ExchangeProviderJcrToSp<FieldValue> , ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    StringSerialization serialization;

    @Override
    public JcrToSp<FieldValue> apply(Node node) {

        try {
            String fieldStr = JcrDaoUtils.getProperty(node, FixedNames.field()).getString();
            Field field = serialization.deserialize(fieldStr, Field.class);
            return applicationContext.getBean(JcrToSp.class.getSimpleName() + field.getValueType(), JcrToSp.class);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
