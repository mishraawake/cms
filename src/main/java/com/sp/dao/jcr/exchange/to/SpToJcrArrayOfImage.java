package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.model.Image;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrArrayOfImage")
public class SpToJcrArrayOfImage implements SpToJcr<FieldValue<Image[]>> {

    @Override
    public void accept(FieldValue<Image[]> spObject, Node node) {
        try {
            Node fileImages = JcrDaoUtils.addNode(node, FixedNames.binaries());
            JcrDaoUtils.addBinariesToNode(fileImages, spObject.getValue(), node.getSession().getValueFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
