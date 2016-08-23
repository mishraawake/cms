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
@Service("SpToJcrChar")
public class SpToJcrChar implements SpToJcr<FieldValue<Character>> {

    @Override
    public void accept(FieldValue<Character> spObject, Node node) {
        try {
            Character aChar = spObject.getValue();
            JcrDaoUtils.setProperty(node, FixedNames.value(),
                    node.getSession().getValueFactory().createValue(aChar));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
