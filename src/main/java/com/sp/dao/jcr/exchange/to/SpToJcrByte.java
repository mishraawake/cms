package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrByte")
public class SpToJcrByte implements SpToJcr<FieldValue<Byte>> {
    @Override
    public void accept(FieldValue<Byte> spObject, Node node) {
        try {
            JcrDaoUtils.setProperty(node, FixedNames.value(), spObject.getValue().byteValue());
        } catch (RepositoryException e){
            throw new RuntimeException(e);
        }
    }
}
