package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import com.sp.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrDefinition")
public class SpToJcrDefinition implements SpToJcr<FieldValue<FieldValue[]>> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    ConsumerProviderSpToJcr<FieldValue> consumerProviderSpToJcr;


    @Override
    public void accept(FieldValue<FieldValue[]> spObject, Node node) {
        try {
            Node valueNode = JcrDaoUtils.addNode(node, FixedNames.value());
            JcrDaoUtils.populateFieldsNodeFromItemFields(Convert.fromArrayToList(spObject.getValue()), valueNode,
                    serialization, consumerProviderSpToJcr);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
