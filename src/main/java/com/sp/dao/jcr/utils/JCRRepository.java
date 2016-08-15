package com.sp.dao.jcr.utils;

import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;

import javax.jcr.*;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class JCRRepository {

    public static final String MY_NAME_SPACE_PREFIX = "sp";
    public static final String PACKAGE_NAME_SPACE_PREFIX = "pkg";
    public static final String MY_NAME_SPACE_URL = "sp.com";

    public static final String PACKAGE_NAME_SPACE_URL = "sp.com/pkg";

    private static Repository repository = null;

    public static Session getSession() throws RepositoryException {
        if(repository == null){
            repository = new Jcr(new Oak()).createRepository();
            initializeRepo();
        }
        return repository.login(new SimpleCredentials("admin", "admin".toCharArray())) ;
    }


    /**
     * Register the name space, node type etc if it already not present.
     * @throws RepositoryException
     */
    private static void initializeRepo() throws RepositoryException {
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray())) ;

        try {
            createNameSpace(session, MY_NAME_SPACE_PREFIX, MY_NAME_SPACE_URL);
            createNameSpace(session, PACKAGE_NAME_SPACE_PREFIX, PACKAGE_NAME_SPACE_URL);
            session.save();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    private static void createNameSpace(Session session, String pkgName, String pkgUrl) throws RepositoryException {
        boolean nameSpaceExists = false;
        for(String pref : session.getNamespacePrefixes())  {
            if(pref.equals(pkgName)){
                nameSpaceExists = true;
            }
        }

        if(!nameSpaceExists) {
            NamespaceHelper namespaceHelper = new NamespaceHelper(session);
            namespaceHelper.registerNamespace(pkgName, pkgUrl);
        }
    }
}
