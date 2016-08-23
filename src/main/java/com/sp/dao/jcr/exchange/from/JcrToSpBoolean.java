package com.sp.dao.jcr.exchange.from;

import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.Value;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpBoolean")
public class JcrToSpBoolean implements JcrToSp<Boolean> {

    @Autowired
    StringSerialization serialization;

    @Override
    public FieldValue<Boolean> apply(Node node) {
        try {
            Value value = JcrDaoUtils.getProperty(node, FixedNames.value()).getValue();
            return FieldValue.getFieldValue( JcrDaoUtils.getField(node, serialization) , value.getBoolean());
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
