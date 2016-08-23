package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrArrayOfFloat")
public class SpToJcrArrayOfFloat implements SpToJcr<FieldValue<float[]>> {

    @Override
    public void accept(FieldValue<float[]> spObject, Node node) {
        try {
            float[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (float eachFloat : array) {
                values[count++] = node.getSession().getValueFactory().createValue(eachFloat);
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
