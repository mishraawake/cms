package com.sp.service.impl;

import com.sp.dao.jcr.model.JCRIdentifiable;
import com.sp.service.IdGenerator;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 09/08/16.
 * <p>
 * It will generate id that will be unique within the branch that this node belong to.
 * It should not be unique globally.
 */
@Service
public class JCRIdGenerator implements IdGenerator<String, JCRIdentifiable> {

    @Override
    public String getNextId(JCRIdentifiable ephemeral) {
        return ephemeral.getIdentityForPath();
    }
}
