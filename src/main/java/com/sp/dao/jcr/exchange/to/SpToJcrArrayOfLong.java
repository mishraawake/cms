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
@Service("SpToJcrArrayOfLong")
public class SpToJcrArrayOfLong implements SpToJcr<FieldValue<long[]>> {

    @Override
    public void accept(FieldValue<long[]> spObject, Node node) {
        try {
            long[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (long eachLong : array) {
                values[count++] = node.getSession().getValueFactory().createValue(eachLong);
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
