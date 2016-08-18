package com.sp.helper;

import com.sp.model.*;
import org.apache.commons.lang.RandomStringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pankajmishra on 18/08/16.
 */
public class PermissionUtils {

       public static Role getDirectiveRole(IItem topItem){
           Role role = new Role();
           role.setName(RandomStringUtils.randomAlphabetic(20));
           Permission
           permission = new Permission(topItem, Privilege.Write);
           role.getPermissions().add(permission);
           permission= new Permission(new Identity() {
               @Override
               public Serializable get__id() {
                   return "/";
               }
           }, Privilege.Read);
           role.getPermissions().add(permission);
           permission= new Permission(topItem.getDefinition(), Privilege.Read);
           role.getPermissions().add(permission);
           return role;
       }


       public static Role getOneChannelDirectiveAndOtherReadRole(IItem ownerChannel, IItem readOnlyChannel){
           Role role = new Role();
           role.setName(RandomStringUtils.randomAlphabetic(20));
           Permission permission= new Permission(ownerChannel, Privilege.Executive);
           role.getPermissions().add(permission);
           permission= new Permission(ownerChannel.getDefinition(), Privilege.Read);
           role.getPermissions().add(permission);

           permission= new Permission(readOnlyChannel, Privilege.Read);
           role.getPermissions().add(permission);



           return role;
      }

    public static Role getComplexRole(IItem ownerChannel, IItem readOnlyChannel, IItem writeChannel){
        Role role = new Role();
        role.setName(RandomStringUtils.randomAlphabetic(20));
        Permission permission= new Permission(ownerChannel, Privilege.Executive);
        role.getPermissions().add(permission);
        permission= new Permission(readOnlyChannel, Privilege.Read);
        role.getPermissions().add(permission);
        permission= new Permission(writeChannel, Privilege.Write);
        role.getPermissions().add(permission);
        permission= new Permission(ownerChannel.getDefinition(), Privilege.Read);
        role.getPermissions().add(permission);
        return role;
    }




}
