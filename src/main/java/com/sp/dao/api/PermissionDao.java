package com.sp.dao.api;

import com.sp.model.IUser;
import com.sp.model.Permission;
import com.sp.model.Role;

import java.util.List;

/**
 * Created by pankajmishra on 17/08/16.
 */
public interface PermissionDao<T> {

    /**
     * Grant permission to user.
     *
     * @param permission
     * @param user
     */
    public void grant(Permission permission, T user) throws DatabaseException;

    /**
     * Revoke permission to user.
     *
     * @param permission
     * @param user
     */
    public void revoke(Permission permission, T user) throws DatabaseException;

    /**
     * Grant permission to group.
     *
     * @param permission
     * @param groupName
     */
    public void grant(Permission permission, String groupName) throws DatabaseException;

    /**
     * Revoke permission to group.
     *
     * @param permission
     * @param groupName
     */
    public void revoke(Permission permission, String groupName) throws DatabaseException;

    /**
     * @param role
     */
    public void defineRole(Role role) throws DatabaseException;

    /**
     * @param role
     */
    public void deleteRole(Role role) throws DatabaseException;

    /**
     * @param role
     * @param user
     */
    public void assignRole(Role role, IUser user) throws DatabaseException;

    /**
     * @param role
     */
    public void assignRole(Role role, String group) throws DatabaseException;

    /**
     *
     */
    public void revokeRole(Role role, IUser user) throws DatabaseException;

    /**
     *
     */
    public void revokeRole(Role role, String group) throws DatabaseException;

    /**
     *
     */
    public List<Role> getAllRole() throws DatabaseException;

    /**
     * @param role
     * @return
     * @throws DatabaseException
     */
    public void updateRole(Role role) throws DatabaseException;

}
