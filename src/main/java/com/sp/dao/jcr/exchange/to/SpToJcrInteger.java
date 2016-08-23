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
@Service("SpToJcrInteger")
public class SpToJcrInteger implements SpToJcr<FieldValue<Integer>> {

    @Override
    public void accept(FieldValue<Integer> spObject, Node node) {
        try {
            Integer aint = spObject.getValue();
            JcrDaoUtils.setProperty(node, FixedNames.value(),
                    node.getSession().getValueFactory().createValue(aint));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    
}
