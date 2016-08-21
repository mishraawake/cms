package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.helper.DefUtils;
import com.sp.model.IDefinition;
import com.sp.model.PojoFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class DefinitionDaoTest extends BaseDaoTest {


    DefinitionDao<IDefinition> definitionDao;


    @Autowired
    PojoFactory pojoFactory;

    @Test
    public void testCreateDefinition() throws DatabaseException {

        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);

        IDefinition definition = DefUtils.getDummyDefinition(pojoFactory);

        definition = definitionDao.create(definition);

        Serializable id = definition.get__id();

        IDefinition definitionChild = DefUtils.getDummyDefinition(pojoFactory);


        definitionChild = definitionDao.create(definitionChild);

        definitionChild = DefUtils.getDummyDefinition(pojoFactory);

        definitionChild = definitionDao.create(definitionChild);

        definitionChild = definitionDao.get(definitionChild.get__id());

        Assert.assertTrue(definitionChild != null);


    }


    @Override
    protected void specificBeforeSetup() throws DatabaseException {

    }

}
