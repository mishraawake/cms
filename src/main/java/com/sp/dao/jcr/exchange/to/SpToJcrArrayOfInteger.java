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
@Service("SpToJcrArrayOfInteger")
public class SpToJcrArrayOfInteger implements SpToJcr<FieldValue<int[]>> {

    @Override
    public void accept(FieldValue<int[]> spObject, Node node) {
        try {
            int[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (int eachInt : array) {
                values[count++] = node.getSession().getValueFactory().createValue(eachInt);
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
