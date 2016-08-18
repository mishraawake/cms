package com.sp.dao.api;

import com.sp.model.IItem;
import com.sp.model.IUser;
import com.sp.model.Permission;
import com.sp.model.Role;

/**
 * Created by pankajmishra on 18/08/16.
 *
 * These permission dao will take care of dependencies of permission.
 *
 * For example .. if a person is given executive right for the creation of node..
 * she must also have to be give read permission for the definition.
 */
public interface SPPermissionDao<U extends IUser> {

    /**
     * Assign permission to the user on item.
     * @param permission
     * @param user
     * @throws DatabaseException
     */
    public void grant(Permission permission, U user) throws DatabaseException;

    /**
     * Assign permission to the group on item.
     * @param permission
     * @param groupName
     * @throws DatabaseException
     */
    public void grant(Permission permission, String groupName) throws DatabaseException;





}
