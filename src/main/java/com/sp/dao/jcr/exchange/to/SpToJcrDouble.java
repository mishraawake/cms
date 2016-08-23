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
@Service("SpToJcrDouble")
public class SpToJcrDouble implements SpToJcr<FieldValue<Double>> {

    @Override
    public void accept(FieldValue<Double> spObject, Node node) {
        try {
            Double aDouble = spObject.getValue();
            JcrDaoUtils.setProperty(node, FixedNames.value(),
                    node.getSession().getValueFactory().createValue(aDouble));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
