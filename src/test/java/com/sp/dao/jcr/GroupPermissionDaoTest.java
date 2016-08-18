package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.helper.DefUtils;
import com.sp.helper.ItemUtils;
import com.sp.helper.UserUtils;
import com.sp.model.*;
import com.sp.service.impl.SimpleSubjectProvider;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class GroupPermissionDaoTest extends BaseDaoTest {


    IDefinition sectionDefinition;

    IDefinition pollDefinition;

    IDefinition articleDefinition;




    IItem<IItem, IDefinition> topSection;

    IItem<IItem, IDefinition> childSection1;

    IItem<IItem, IDefinition> childSection2;

    IItem<IItem, IDefinition> gchildSection11;

    IItem<IItem, IDefinition> gchildSection12;

    IItem<IItem, IDefinition> gchildSection21;

    IItem<IItem, IDefinition> gchildSection22;



    @Override
    protected void specificBeforeSetup() throws DatabaseException {
        // create few definition
        sectionDefinition = DefUtils.getDummyDefition(pojoFactory);
        pollDefinition = DefUtils.getDummyDefition(pojoFactory);
        articleDefinition = DefUtils.getDummyDefition(pojoFactory);
        sectionDefinition = definitionDao.create(sectionDefinition);
        pollDefinition = definitionDao.create(pollDefinition);
        articleDefinition = definitionDao.create(articleDefinition);

        topSection = ItemUtils.getDummyItem(pojoFactory);
        topSection.setDefinition(sectionDefinition);
        childSection1 = ItemUtils.getDummyItem(pojoFactory);
        childSection1.setDefinition(sectionDefinition);
        gchildSection11 = ItemUtils.getDummyItem(pojoFactory);
        gchildSection11.setDefinition(sectionDefinition);
        gchildSection12 = ItemUtils.getDummyItem(pojoFactory);
        gchildSection12.setDefinition(sectionDefinition);

        childSection2 = ItemUtils.getDummyItem(pojoFactory);
        childSection2.setDefinition(sectionDefinition);
        gchildSection21 = ItemUtils.getDummyItem(pojoFactory);
        gchildSection21.setDefinition(sectionDefinition);
        gchildSection22 = ItemUtils.getDummyItem(pojoFactory);
        gchildSection22.setDefinition(sectionDefinition);




    }

    @Test
    public void testWriteRolePositive() throws DatabaseException {

        // we are doing this as admin.
        IUser adminitrator  = UserUtils.getDummyUser(pojoFactory);
        // add user iadministrator.
        adminitrator = userDao.create(adminitrator);

        topSection = itemDao.create(topSection);

        String groupName = RandomStringUtils.randomAlphabetic(20);
        userDao.createGroup(groupName);

        Permission permission = new Permission(topSection, Privilege.Executive);

        spPermissionDao.grant(permission, groupName);

        // assign user administrator into above group

        userDao.addUserToGroup(adminitrator, groupName);

        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, adminitrator);
        // from now on administrator can add , remove , or do anything on any kind of operations.

        childSection1.setParentItem(topSection);

        itemDao.create(childSection1);
    }



    @Test(expected = DatabaseException.class)
    public void testWriteRoleRevoke() throws DatabaseException {
        // we are doing this as admin.
        IUser adminitrator  = UserUtils.getDummyUser(pojoFactory);
        // add user iadministrator.
        adminitrator = userDao.create(adminitrator);

        topSection = itemDao.create(topSection);

        String groupName = RandomStringUtils.randomAlphabetic(20);
        userDao.createGroup(groupName);

        Permission permission = new Permission(topSection, Privilege.Executive);

        spPermissionDao.grant(permission, groupName);

        // assign user administrator into above group

        userDao.addUserToGroup(adminitrator, groupName);

        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, adminitrator);
        // from now on administrator can add , remove , or do anything on any kind of operations.

        childSection1.setParentItem(topSection);

        itemDao.create(childSection1);


        // login as admin
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, UserUtils.getAdminUser(pojoFactory));


        // now he is no longer administrator.
        permissionDao.revoke(permission, groupName);

        // loging again as administrator.
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, adminitrator);

        childSection2.setParentItem(topSection);

        itemDao.create(childSection2);

    }


   @Test(expected = DatabaseException.class)
    public void testWriteRoleNegative() throws DatabaseException {

       // we are doing this as admin.
       IUser adminitrator  = UserUtils.getDummyUser(pojoFactory);
       // add user iadministrator.
       adminitrator = userDao.create(adminitrator);

       topSection = itemDao.create(topSection);

       String groupName = RandomStringUtils.randomAlphabetic(20);
       userDao.createGroup(groupName);

       Permission permission = new Permission(topSection, Privilege.Read);

       spPermissionDao.grant(permission, groupName);

       // assign user administrator into above group

       userDao.addUserToGroup(adminitrator, groupName);

       runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, adminitrator);
       // from now on administrator can add , remove , or do anything on any kind of operations.

       childSection1.setParentItem(topSection);

       itemDao.create(childSection1);
    }



    @Test()
    public void testReadRolPositive() throws DatabaseException {

        // we are doing this as admin.
        IUser user  = UserUtils.getDummyUser(pojoFactory);
        // add user iadministrator.
        user = userDao.create(user);

        topSection = itemDao.create(topSection);

        String groupName = RandomStringUtils.randomAlphabetic(20);
        userDao.createGroup(groupName);

        Permission permission = new Permission(topSection, Privilege.Read);

        spPermissionDao.grant(permission, groupName);

        // assign user administrator into above group

        userDao.addUserToGroup(user, groupName);

        childSection1.setParentItem(topSection);

        childSection1 = itemDao.create(childSection1);
        // now on user will change to user. For the time being this is the dirty trick to change the user.
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, user);

        childSection1 = itemDao.get(childSection1.get__id());

        topSection = itemDao.get(topSection.get__id());

    }

    @Test(expected = DatabaseException.class)
    public void testReadRolNegative() throws DatabaseException {

        // we are doing this as admin.
        IUser user  = UserUtils.getDummyUser(pojoFactory);
        // add user iadministrator.
        user = userDao.create(user);

        topSection = itemDao.create(topSection);

        String groupName = RandomStringUtils.randomAlphabetic(20);
        userDao.createGroup(groupName);

        childSection1.setParentItem(topSection);

        childSection1 = itemDao.create(childSection1);

        Permission permission = new Permission(childSection1, Privilege.Read);

        spPermissionDao.grant(permission, groupName);

        // assign user administrator into above group

        userDao.addUserToGroup(user, groupName);


        // now on user will change to user. For the time being this is the dirty trick to change the user.
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, user);

        childSection1 = itemDao.get(childSection1.get__id());

        topSection = itemDao.get(topSection.get__id());

    }







}
