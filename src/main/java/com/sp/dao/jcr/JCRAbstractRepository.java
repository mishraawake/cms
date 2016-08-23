package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DatabaseInitException;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.model.IUser;
import com.sp.service.SubjectProvider;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Created by pankajmishra on 21/08/16.
 */
public abstract class JCRAbstractRepository implements JCRIRepository {



    @Autowired
    SubjectProvider subjectProvider;

    protected Repository repository = null;

    private final String ADMIN_USER_NAME = "admin";

    @PostConstruct
    final private void initialization() throws DatabaseException {
        initializeRepo();
        initializeRepoSetup();
    }

    public Session getSession() throws DatabaseException {
        try {
            IUser user = subjectProvider.getCurrentSubject();
            if (user == null) {
                throw new DatabaseInitException(new RuntimeException("Database is being accessed by anonymous user"));
            }
            return repository.login(new SimpleCredentials(getUserName(user.getUserName()), user.getPassword()));
        } catch (Exception e){
            throw new DatabaseInitException(e);
        }
    }

    final private String getUserName(String userName){
        if(ADMIN_USER_NAME.equals(userName)){
            return userName;
        }
        return JcrNameFac.getUserMgmtName(userName).name();
    }



    protected abstract void initializeRepo() throws DatabaseException ;

    /**
     * Register the name space, node type etc if it already not present.
     *
     * @throws javax.jcr.RepositoryException
     */
    final private void initializeRepoSetup() throws DatabaseException {
        createNameSpaces();
    }

    final private void createNameSpaces()  throws DatabaseException {
        Session session = getSession();

        try {
            for(String[] nameSpace : NAME_SPACES){
                createNameSpace(session, nameSpace[0], nameSpace[1]);
            }
            session.save();
        } catch (Exception e) {
            throw new DatabaseInitException(e);
        } finally {
            session.logout();
        }
    }

    final private void createNameSpace(Session session, String pkgName, String pkgUrl) throws RepositoryException {
        boolean nameSpaceExists = false;
        for (String pref : session.getNamespacePrefixes()) {
            if (pref.equals(pkgName)) {
                nameSpaceExists = true;
            }
        }
        if (!nameSpaceExists) {
            NamespaceHelper namespaceHelper = new NamespaceHelper(session);
            namespaceHelper.registerNamespace(pkgName, pkgUrl);
        }
    }
}
