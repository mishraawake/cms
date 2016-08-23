package com.sp.helper;

import com.sp.model.IItem;
import com.sp.model.Permission;
import com.sp.model.Privilege;
import com.sp.model.Role;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by pankajmishra on 18/08/16.
 */
public class PermissionUtils {

    public static Role getDirectiveRole(IItem topItem) {
        Role role = new Role();
        role.setName(RandomStringUtils.randomAlphabetic(20));
        Permission
                permission = new Permission(topItem.get__id(), Privilege.Write, true);
        role.getPermissions().add(permission);
        permission = new Permission("/", Privilege.Read, true);
        role.getPermissions().add(permission);
        permission = new Permission(topItem.getDefinition().get__id(), Privilege.Read, true);
        role.getPermissions().add(permission);
        return role;
    }

    public static Permission getDummyPermission() {
        Permission
                permission = new Permission(RandomStringUtils.randomAlphabetic(100), Privilege.Write, true);
        return permission;
    }


    public static Role getOneChannelDirectiveAndOtherReadRole(IItem ownerChannel, IItem readOnlyChannel) {
        Role role = new Role();
        role.setName(RandomStringUtils.randomAlphabetic(20));
        Permission permission = new Permission(ownerChannel.get__id(), Privilege.Executive, true);
        role.getPermissions().add(permission);
        permission = new Permission(ownerChannel.getDefinition().get__id(), Privilege.Read, true);
        role.getPermissions().add(permission);

        permission = new Permission(readOnlyChannel.get__id(), Privilege.Read, true);
        role.getPermissions().add(permission);


        return role;
    }

    public static Role getComplexRole(IItem ownerChannel, IItem readOnlyChannel, IItem writeChannel) {
        Role role = new Role();
        role.setName(RandomStringUtils.randomAlphabetic(20));
        Permission permission = new Permission(ownerChannel.get__id(), Privilege.Executive, true);
        role.getPermissions().add(permission);
        permission = new Permission(readOnlyChannel.get__id(), Privilege.Read, true);
        role.getPermissions().add(permission);
        permission = new Permission(writeChannel.get__id(), Privilege.Write, true);
        role.getPermissions().add(permission);
        permission = new Permission(ownerChannel.getDefinition().get__id(), Privilege.Read, true);
        role.getPermissions().add(permission);
        return role;
    }


}
