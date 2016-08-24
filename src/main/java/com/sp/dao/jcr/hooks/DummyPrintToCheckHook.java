package com.sp.dao.jcr.hooks;

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

/**
 * Created by pankajmishra on 24/08/16.
 */
@CallPoint(classNames = {@ClassPoint(target = "ItemDao" , methodNames = {})})
@Service
public class DummyPrintToCheckHook implements BeforeWrite<JCRItem, Integer> {

    @Autowired
    ValidatorProviderImpl validatorProvider;

    @Override
    public boolean proceed(JCRItem spObject) {
        return false;
    }

    @Override
    public Integer rank() {
        return 1;
    }
}
