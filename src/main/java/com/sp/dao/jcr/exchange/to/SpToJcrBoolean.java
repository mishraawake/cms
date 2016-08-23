package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrBoolean")
public class SpToJcrBoolean implements SpToJcr<FieldValue<Boolean>> {
    @Override
    public void accept(FieldValue<Boolean> spObject, Node node) {
        try {
            JcrDaoUtils.setProperty(node, FixedNames.value(), spObject.getValue().booleanValue());
        } catch (RepositoryException e){
            throw new RuntimeException(e);
        }
    }
}
