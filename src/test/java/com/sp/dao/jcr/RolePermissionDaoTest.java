package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.helper.DefUtils;
import com.sp.helper.ItemUtils;
import com.sp.helper.PermissionUtils;
import com.sp.helper.UserUtils;
import com.sp.model.IDefinition;
import com.sp.model.IItem;
import com.sp.model.IUser;
import com.sp.model.Role;
import com.sp.service.impl.SimpleSubjectProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RolePermissionDaoTest extends BaseDaoTest {


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
        sectionDefinition = DefUtils.getDummyDefinition(pojoFactory);
        pollDefinition = DefUtils.getDummyDefinition(pojoFactory);
        articleDefinition = DefUtils.getDummyDefinition(pojoFactory);
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
    public void testCreateRole() throws DatabaseException {


        topSection = itemDao.create(topSection);
        childSection1.setParentItem(topSection);
        childSection1 = itemDao.create(childSection1);
        gchildSection11.setParentItem(childSection1);

        gchildSection11 = itemDao.create(gchildSection11);
        childSection2.setParentItem(topSection);
        childSection2 = itemDao.create(childSection2);


        permissionDao.defineRole(PermissionUtils.getDirectiveRole(topSection));
        permissionDao.defineRole(PermissionUtils.getOneChannelDirectiveAndOtherReadRole(childSection1, childSection2));
        permissionDao.defineRole(PermissionUtils.getComplexRole(childSection2, childSection1, gchildSection11));


        List<Role> roles = permissionDao.getAllRole();

        Assert.assertTrue("size should be 3", roles.size() >= 3);
    }


    @Test
    public void testDeleteRole() throws DatabaseException {


        topSection = itemDao.create(topSection);
        childSection1.setParentItem(topSection);
        childSection1 = itemDao.create(childSection1);
        gchildSection11.setParentItem(childSection1);

        gchildSection11 = itemDao.create(gchildSection11);
        childSection2.setParentItem(topSection);
        childSection2 = itemDao.create(childSection2);


        Role topRole = PermissionUtils.getDirectiveRole(topSection);
        permissionDao.defineRole(topRole);
        permissionDao.defineRole(PermissionUtils.getOneChannelDirectiveAndOtherReadRole(childSection1, childSection2));
        permissionDao.defineRole(PermissionUtils.getComplexRole(childSection2, childSection1, gchildSection11));

        permissionDao.deleteRole(topRole);

        List<Role> roles = permissionDao.getAllRole();

        Assert.assertTrue("size should be greater than 2 ", roles.size() >= 2);
    }


    @Test
    public void testRoleBasedPermission() throws DatabaseException {

        topSection = itemDao.create(topSection);
        childSection1.setParentItem(topSection);
        childSection1 = itemDao.create(childSection1);
        gchildSection11.setParentItem(childSection1);

        gchildSection11 = itemDao.create(gchildSection11);
        childSection2.setParentItem(topSection);
        childSection2 = itemDao.create(childSection2);


        Role directorRole = PermissionUtils.getDirectiveRole(topSection);

        Role vpRole = PermissionUtils.getOneChannelDirectiveAndOtherReadRole(childSection1, childSection2);

        Role engineerRole = PermissionUtils.getComplexRole(childSection2, childSection1, gchildSection11);

        permissionDao.defineRole(directorRole);
        permissionDao.defineRole(vpRole);
        permissionDao.defineRole(engineerRole);


        IUser director = UserUtils.getDummyUser(pojoFactory), vp = UserUtils.getDummyUser(pojoFactory),
                engineer = UserUtils.getDummyUser(pojoFactory);

        director.setUserName("director");
        director = userDao.create(director);

        permissionDao.assignRole(directorRole, director);

        vp.setUserName("vp");
        vp = userDao.create(vp);

        permissionDao.assignRole(vpRole, vp);


        engineer.setUserName("engineer");
        engineer = userDao.create(engineer);

        permissionDao.assignRole(engineerRole, engineer);

        // director should be able to add a new child in top section.
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, director);


        IItem<IItem, IDefinition> childSection3 = ItemUtils.getDummyItem(pojoFactory);
        childSection3.setDefinition(sectionDefinition);
        childSection3 = itemDao.createChild(childSection3, topSection);


        // director should not be able to read the properties of top section.
        topSection = itemDao.get(topSection.get__id());


        // vp should be able to add new child in childSection1 and not in childSection2
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, vp);


        itemDao.createChild(gchildSection12, childSection1);
        try {
            itemDao.createChild(gchildSection21, childSection2);
        } catch (DatabaseException e) {

        }


        // vp should be able to add new child in childSection2 and not in childSection1 but can add item in
        // gchildSection11
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, engineer);


        gchildSection12 = ItemUtils.getDummyItem(pojoFactory);
        gchildSection12.setDefinition(topSection.getDefinition());
        itemDao.createChild(gchildSection12, childSection2);
        try {
            itemDao.createChild(gchildSection21, childSection1);
        } catch (DatabaseException e) {
        }

        IItem<IItem, IDefinition> ggchild111 = ItemUtils.getDummyItem(pojoFactory);
        ggchild111.setDefinition(gchildSection11.getDefinition());


        ggchild111.setFieldValues(new ArrayList<>());
        itemDao.createChild(ggchild111, gchildSection11);


        // engineer should be able to add new child in childSection2 and not in childSection1 but should be able to
        // update gchildSection11


        List<Role> roles = permissionDao.getAllRole();
    }


}
