package com.sp.dao.jcr.exchange.to;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.model.FieldValue;
import org.springframework.stereotype.Service;

import javax.jcr.Node;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("SpToJcrRef")
public class SpToJcrRef implements SpToJcr<FieldValue<String>> {

    @Override
    public void accept(FieldValue<String> spObject, Node node) {
        //TODO
    }
    
}
