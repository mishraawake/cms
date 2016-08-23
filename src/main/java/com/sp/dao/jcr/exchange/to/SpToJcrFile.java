package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.model.FileObject;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrFile")
public class SpToJcrFile implements SpToJcr<FieldValue<FileObject>> {

    @Override
    public void accept(FieldValue<FileObject> spObject, Node node) {
        try {
            Node fileObjects = JcrDaoUtils.addNode(node, FixedNames.binaries());
            JcrDaoUtils.addBinaryToNode(fileObjects, spObject.getValue(), node.getSession().getValueFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
