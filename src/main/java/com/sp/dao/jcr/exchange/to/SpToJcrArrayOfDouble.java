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
@Service("SpToJcrArrayOfDouble")
public class SpToJcrArrayOfDouble implements SpToJcr<FieldValue<double[]>> {

    @Override
    public void accept(FieldValue<double[]> spObject, Node node) {
        try {
            double[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (double eachDouble : array) {
                values[count++] = node.getSession().getValueFactory().createValue(eachDouble);
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
