package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.model.Video;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrArrayOfVideo")
public class SpToJcrArrayOfVideo implements SpToJcr<FieldValue<Video[]>> {

    @Override
    public void accept(FieldValue<Video[]> spObject, Node node) {
        try {
            Node fileVideos = JcrDaoUtils.addNode(node, FixedNames.binaries());
            JcrDaoUtils.addBinariesToNode(fileVideos, spObject.getValue(), node.getSession().getValueFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
