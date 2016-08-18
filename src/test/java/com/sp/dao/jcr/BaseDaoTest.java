package com.sp.dao.jcr;

import com.sp.dao.api.*;
import com.sp.helper.UserUtils;
import com.sp.model.*;
import com.sp.service.RunTimeContext;
import com.sp.service.impl.SimpleSubjectProvider;
import com.sp.utils.SpringInitializer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by pankajmishra on 18/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public abstract class BaseDaoTest implements ApplicationContextAware {

    ApplicationContext applicationContext;


    DefinitionDao<IDefinition> definitionDao;

    @Autowired
    PojoFactory pojoFactory;

    @Autowired
    RunTimeContext<IUser> runTimeContext;

    PermissionDao<IUser> permissionDao;

    SPPermissionDao<IUser> spPermissionDao;


    ItemDao<IItem> itemDao;

    UserDao<IUser> userDao;


    @Before
    public void setup() throws DatabaseException {
        /**
         * Because all the dao classes are generic of generic so spring autowiring is not helping. Need to define this
         * into spring.xml
         */
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, UserUtils.getAdminUser(pojoFactory));
        userDao = applicationContext.getBean("userDao", UserDao.class);
        itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        permissionDao = applicationContext.getBean("permissionDao", PermissionDao.class);
        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);
        spPermissionDao = applicationContext.getBean("spPermissionDao", SPPermissionDao.class);
        specificBeforeSetup();
    }


    protected abstract void specificBeforeSetup() throws DatabaseException;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
