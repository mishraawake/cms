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
@Service("SpToJcrDate")
public class SpToJcrDate implements SpToJcr<FieldValue<Date>> {

    @Override
    public void accept(FieldValue<Date> spObject, Node node) {
        try {
            JcrDaoUtils.setProperty(node, FixedNames.value(),
                    node.getSession().getValueFactory().
                            createValue(JcrDaoUtils.createCalender(spObject.getValue())));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
