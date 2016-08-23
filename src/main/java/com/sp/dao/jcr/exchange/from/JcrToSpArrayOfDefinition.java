package com.sp.dao.jcr.exchange.from;

import com.sp.dao.api.exchange.ExchangeProviderJcrToSp;
import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.dao.jcr.utils.UserDaoUtils;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import com.sp.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpArrayOfDefinition")
public class JcrToSpArrayOfDefinition implements JcrToSp<FieldValue[][]> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    ExchangeProviderJcrToSp<FieldValue> exchangeProviderJcrToSp;

    @Override
    public FieldValue<FieldValue[][]> apply(Node node) {
        try {
            NodeIterator valueNodeIterator = node.getNodes(JcrNameFac.getProjectName("*").pattern());
            List<FieldValue[]> result = new ArrayList<>();
            while (valueNodeIterator.hasNext()){
                List<FieldValue> fieldValueList = JcrDaoUtils.getFieldsFromNode(valueNodeIterator.nextNode(), serialization, exchangeProviderJcrToSp);
                result.add(fieldValueList.toArray(new FieldValue[fieldValueList.size()]));
            }
            return FieldValue.getFieldValue(JcrDaoUtils.getField(node, serialization), result.toArray(new
                    FieldValue[0][0])) ;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
