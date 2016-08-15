package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.model.IDefinition;
import com.sp.model.PojoFactory;
import com.sp.utils.DefUtils;
import com.sp.utils.SpringInitializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

/**
 * Created by pankajmishra on 07/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class DefinitionDaoTest implements ApplicationContextAware {

    ApplicationContext applicationContext;


    DefinitionDao<IDefinition> definitionDao;


    @Autowired
    PojoFactory pojoFactory;

    @Test
    public void testCreateDefinition(){

        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);

        IDefinition definition = DefUtils.getDummyDefition(pojoFactory);
        try {
            definition =  definitionDao.create(definition);

            Serializable id = definition.get__id();

            IDefinition definitionChild = DefUtils.getDummyDefition(pojoFactory);


            definitionChild = definitionDao.create(definitionChild);

            definitionChild = DefUtils.getDummyDefition(pojoFactory);

            definitionChild = definitionDao.create(definitionChild);

            definitionChild = definitionDao.get(definitionChild.get__id());

            Assert.assertTrue(definitionChild != null);

        } catch (DatabaseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
