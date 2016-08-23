package com.sp.dao.jcr.exchange.from;

import com.sp.dao.api.exchange.ExchangeProviderJcrToSp;
import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.model.ValueType;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpDefinition")
public class JcrToSpDefinition implements JcrToSp<FieldValue[]> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    ExchangeProviderJcrToSp<FieldValue> exchangeProviderJcrToSp;

    @Override
    public FieldValue<FieldValue[]> apply(Node node) {
        try {
            Node fieldNode = JcrDaoUtils.getNode(node, FixedNames.value());
            List<FieldValue> fieldValueList = JcrDaoUtils.getFieldsFromNode(fieldNode, serialization, exchangeProviderJcrToSp);
            return FieldValue.getFieldValue(JcrDaoUtils.getField(node, serialization), fieldValueList.toArray(new FieldValue[fieldValueList.size()])) ;

        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
