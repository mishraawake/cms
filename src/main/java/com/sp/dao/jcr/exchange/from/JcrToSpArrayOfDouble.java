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

/*
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpArrayOfDouble")
public class JcrToSpArrayOfDouble implements JcrToSp<double[]> {

    @Autowired
    StringSerialization serialization;

    @Override
    public FieldValue<double[]> apply(Node node) {
        try {

            Value[] values = JcrDaoUtils.getProperty(node, FixedNames.value()).getValues();
            double[] array = new double[values.length];
            int count = 0;
            for (Value eachValue : values) {
                array[count++] = eachValue.getDouble();
            }
            return FieldValue.getFieldValue( JcrDaoUtils.getField(node, serialization) , array);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
