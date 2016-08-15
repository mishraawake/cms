package com.sp.dao.api;

import com.sp.model.IUser;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pankajmishra on 15/08/16.
 */
public interface UserDao<T extends IUser> extends IDao<T> {
    void createGroup(String groupName) throws DatabaseException;
    void removeGroup(String groupName) throws DatabaseException;
    Iterator<T> getMemberOf(String groupName) throws DatabaseException;
    List<String> getGroupOf(String userName) throws DatabaseException;

    /**
     * It also create user and group if they are not available.
     * @param user
     * @param groupName
     * @throws DatabaseException
     */
    void addUserToGroup(T user, String groupName) throws DatabaseException;
}
