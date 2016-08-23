package com.sp.dao.jcr.exchange.from;

import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.*;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import java.util.ArrayList;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpArrayOfImage")
public class JcrToSpArrayOfImage implements JcrToSp<Image[]> {

    @Autowired
    StringSerialization serialization;

    @Override
    public FieldValue<Image[]> apply(Node node) {
        try {

            Node binaryContainingNode = JcrDaoUtils.getNode(node, FixedNames.binaries());

            return FieldValue.getFieldValue(JcrDaoUtils.getField(node, serialization), JcrDaoUtils.getBinariesFromNode
                    (binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.IMAGE_FACTORY));

        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
