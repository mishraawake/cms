package com.sp.dao.api;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface JCRIRepository {

    public static final String MY_NAME_SPACE_PREFIX = "sp";
    public static final String PACKAGE_NAME_SPACE_PREFIX = "pkg";
    public static final String MY_NAME_SPACE_URL = "sp.com";
    public static final String PACKAGE_NAME_SPACE_URL = "sp.com/pkg";

    public Session getSession() throws RepositoryException;

    public void shutDown();

}
