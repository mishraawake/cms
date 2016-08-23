package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrArrayOfRef")
public class SpToJcrArrayOfRef implements SpToJcr<FieldValue<String[]>> {

    @Override
    public void accept(FieldValue<String[]> spObject, Node node) {
        //TODO
    }
    
}
