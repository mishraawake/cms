package com.sp.dao.jcr;

import com.sp.dao.api.JCRIRepository;
import com.sp.model.IUser;
import com.sp.service.SubjectProvider;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository
public class JCRMemoryRepository implements JCRIRepository {

    @Autowired
    SubjectProvider subjectProvider;


    private static Repository repository = null;

    public Session getSession() throws RepositoryException {
        if(repository == null){
            repository = new Jcr(new Oak()).createRepository();
            initializeRepo();
        }
        IUser user = subjectProvider.getCurrentSubject();
        if(user == null){
            throw new RuntimeException("Database is being accessed by anonymous user");
        }
        return repository.login(new SimpleCredentials(user.getUserName(), user.getPassword())) ;
    }


    /**
     * Register the name space, node type etc if it already not present.
     * @throws RepositoryException
     */
    private void initializeRepo() throws RepositoryException {
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
