package com.sp.dao.jcr.exchange.from;

import com.sp.dao.api.exchange.JcrToSp;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.BinaryFactory;
import com.sp.model.FieldValue;
import com.sp.model.Image;
import com.sp.model.Video;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("JcrToSpVideo")
public class JcrToSpVideo implements JcrToSp<Video> {

    @Autowired
    StringSerialization serialization;

    @Override
    public FieldValue<Video> apply(Node node) {
        try {

            Node binaryContainingNode = JcrDaoUtils.getNode(node, FixedNames.binaries());
            return FieldValue.getFieldValue(JcrDaoUtils.getField(node, serialization), JcrDaoUtils.getBinaryFromNode
                    (binaryContainingNode, BinaryFactory.VIDEO_FACTORY.getObject()));
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
