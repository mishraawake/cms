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
@Service("SpToJcrArrayOfByte")
public class SpToJcrArrayOfByte implements SpToJcr<FieldValue<byte[]>> {

    @Override
    public void accept(FieldValue<byte[]> spObject, Node node) {
        try {
            byte[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (byte eachByte : array) {
                values[count++] = node.getSession().getValueFactory().createValue(eachByte);
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

}
