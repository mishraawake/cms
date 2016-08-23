package com.sp.dao.api;

import javax.jcr.Session;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface JCRIRepository {

    public static final String[][] NAME_SPACES = {
            // primary name space for this cms application.
            {"sp","sp.com"},
            // a name space used for packaging that is different from normal hierarchy
            {"pkg","sp.com/pkg"},
            // all the user defined name will be there in this name space. So that conflict would never arise from
            // our name to user name. Note that two names of user provided name could well conflict.
            {"udn","sp.com/udn"},
            {"umgmt","sp.com/umgmt"},
            {"role","sp.com/role"},
            {"group","sp.com/group"},
    };

    static final String PROJECT_NAME_SPACE_PREFIX = NAME_SPACES[0][0];
    static final String PACKAGE_NAME_SPACE_PREFIX = NAME_SPACES[1][0];
    static final String USER_DEFINED_NAME_SPACE_PREFIX = NAME_SPACES[2][0];
    static final String USER_MGMT_SPACE_PREFIX = NAME_SPACES[3][0];
    static final String USER_ROLE_SPACE_PREFIX = NAME_SPACES[4][0];
    static final String GROUP_ROLE_SPACE_PREFIX = NAME_SPACES[4][0];

    public Session getSession() throws DatabaseException;

    public void shutDown();

}
