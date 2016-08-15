package com.sp.dao.jcr;

import com.google.common.collect.Lists;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.api.ItemDao;
import com.sp.dao.api.UserDao;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.helper.UserUtils;
import com.sp.model.*;
import com.sp.utils.FieldUtils;
import com.sp.utils.SpringInitializer;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pankajmishra on 15/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class UserDaoTest implements ApplicationContextAware {

    ApplicationContext applicationContext;

    UserDao<IUser> userDao;

    @Autowired
    PojoFactory pojoFactory;

    @Before
    public void prepareDependencies() {
        userDao = applicationContext.getBean("userDao", UserDao.class);
    }


    @Test
    public void testUserCreation() throws DatabaseException {
        IUser iUser = UserUtils.getDummyUser(pojoFactory);
        userDao.create(iUser);
        IUser dbUser = userDao.get(iUser.getUserName());
        verifyUsers(iUser, dbUser);
    }

    private void verifyUsers(IUser first, IUser second) {
        Assert.assertTrue(String.format("user name should be same %s, %s", first.getUserName(), second.getUserName()),
                first.getUserName().equals(second.getUserName())
        );

        FieldUtils.verifyFieldLists(first.getProperties().toArray(new FieldValue[0]), second.getProperties().toArray(new FieldValue[0]));
    }

    @Test
    public void testGroupCreation() throws DatabaseException {

        String group = RandomStringUtils.randomAlphabetic(10);
        userDao.createGroup(group);
        Assert.assertTrue(String.format("group name should be there in db %s", group), userDao.groupExists(group));
    }

    @Test
    public void testUserRemove() throws DatabaseException {
        IUser iUser = UserUtils.getDummyUser(pojoFactory);
        userDao.create(iUser);
        IUser dbUser = userDao.get(iUser.getUserName());
        verifyUsers(iUser, dbUser);
        userDao.delete(iUser.getUserName());
        Assert.assertFalse(String.format("user should be deleted %s", iUser.getUserName()), userDao.exists(iUser.getUserName()));
    }

    @Test
    public void testGroupRemove() throws DatabaseException {
        String group = RandomStringUtils.randomAlphabetic(10);
        userDao.createGroup(group);
        Assert.assertTrue(String.format("group name should be there in db %s", group), userDao.groupExists(group));
        userDao.removeGroup(group);
        Assert.assertFalse(String.format("group name should not be there in db %s", group), userDao.groupExists(group));
    }

    @Test
    public void testGroupsOfUser() throws DatabaseException {
        IUser iUser = UserUtils.getDummyUser(pojoFactory);
        String[] groups = {RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)};
        for (String group : groups) {
            userDao.addUserToGroup(iUser, group);
        }
        IUser dbUser = userDao.get(iUser.getUserName());
        verifyUsers(iUser, dbUser);

        List<String> dbGroups = userDao.getGroupOf(iUser.getUserName());

        String[] dbGroupsArray = dbGroups.toArray(new String[0]);

        Arrays.sort(groups);
        Arrays.sort(dbGroupsArray);

        Assert.assertTrue(String.format("ggroup names should be same %s, %s", Arrays.toString(groups),
                Arrays.toString(dbGroupsArray)), Arrays.equals(groups, dbGroupsArray));
    }

    @Test
    public void testUsersOfGroup() throws DatabaseException {
        IUser iUser1 = UserUtils.getDummyUser(pojoFactory), iUser2 = UserUtils.getDummyUser(pojoFactory);
        String group = RandomStringUtils.randomAlphabetic(10);
        userDao.addUserToGroup(iUser1, group);
        userDao.addUserToGroup(iUser2, group);

        IUser dbUser1 = userDao.get(iUser1.getUserName());
        verifyUsers(iUser1, dbUser1);
        IUser dbUser2 = userDao.get(iUser2.getUserName());
        verifyUsers(iUser2, dbUser2);

        Iterator<IUser> dbusers = userDao.getMemberOf(group);

        int x = 0;
        while (dbusers.hasNext()) {
            dbUser1 = dbusers.next();
            if (x == 0) {
                verifyUsers(iUser1, dbUser1);
            }
            if (x == 1) {
                verifyUsers(iUser2, dbUser2);
            }
            ++x;
        }
    }


    @Test
    public void testUsersList() throws DatabaseException {
        IUser iUser1 = UserUtils.getDummyUser(pojoFactory), iUser2 = UserUtils.getDummyUser(pojoFactory);
        userDao.create(iUser1);
        userDao.create(iUser2);

        IUser dbUser1 = userDao.get(iUser1.getUserName());
        verifyUsers(iUser1, dbUser1);
        IUser dbUser2 = userDao.get(iUser2.getUserName());
        verifyUsers(iUser2, dbUser2);

        Iterable<IUser> dbusers = userDao.list();

        List<IUser> lists = Lists.newArrayList(dbusers);

        Assert.assertTrue(String.format("list size should be 2 instead it is %d", lists.size()), lists.size() >= 2);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
