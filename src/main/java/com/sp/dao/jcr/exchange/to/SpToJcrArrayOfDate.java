package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Date;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrArrayOfDate")
public class SpToJcrArrayOfDate implements SpToJcr<FieldValue<Date[]>> {

    @Override
    public void accept(FieldValue<Date[]> spObject, Node node) {
        try {
            Date[] array = spObject.getValue();
            Value[] values = new Value[array.length];
            int count = 0;
            for (Date eachDate : array) {
                values[count++] = node.getSession().getValueFactory().
                        createValue(JcrDaoUtils.createCalender(eachDate));
            }
            JcrDaoUtils.setProperty(node, FixedNames.value(), values);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
