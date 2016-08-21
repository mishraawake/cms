package com.sp.dao.jcr.model;

import com.sp.model.IDefinition;
import com.sp.model.IItem;
import com.sp.model.IUser;
import com.sp.model.PojoFactory;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 10/08/16.
 */
@Service(value = "pojoFactory")
public class JCRPojoFactory implements PojoFactory {

    @Override
    public IDefinition getNewDefinition() {
        return new JCRDefinition();
    }

    @Override
    public IItem getNewItem() {
        return new JCRItem();
    }

    @Override
    public IUser getNewUser() {
        return new JCRUser();
    }
}
