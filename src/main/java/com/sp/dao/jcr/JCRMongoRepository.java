package com.sp.dao.jcr;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.sp.dao.api.JCRIRepository;
import com.sp.model.IUser;
import com.sp.service.SubjectProvider;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository
public class JCRMongoRepository implements JCRIRepository {

    @Autowired
    SubjectProvider subjectProvider;

    final static String MONGO_SERVER_IP = "127.0.0.1";

    private static String MONGO_DATABASE_NAME = "cms";

    DocumentNodeStore dns = null;

    private Repository repository = null;

    @PostConstruct
    private void initializeRepository() throws Exception {
        DB db = new Mongo(MONGO_SERVER_IP, 27017).getDB(MONGO_DATABASE_NAME);
        dns = new DocumentMK.Builder().
                setMongoDB(db).getNodeStore();
        repository = new Jcr(new Oak(dns)).createRepository();
        initializeRepo();
    }


    public Session getSession() throws RepositoryException {
        try {

            IUser user = subjectProvider.getCurrentSubject();
            if (user == null) {
                throw new RuntimeException("Database is being accessed by anonymous user");
            }
            Session session = repository.login(new SimpleCredentials(user.getUserName(), user.getPassword()));
            return session;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    @PreDestroy
    public void shutDown() {
        if (dns != null) {
            //  dns.dispose();
        }
    }


    /**
     * Register the name space, node type etc if it already not present.
     *
     * @throws javax.jcr.RepositoryException
     */
    private void initializeRepo() throws RepositoryException {
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

        try {
            createNameSpace(session, MY_NAME_SPACE_PREFIX, MY_NAME_SPACE_URL);
            createNameSpace(session, PACKAGE_NAME_SPACE_PREFIX, PACKAGE_NAME_SPACE_URL);
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    private static void createNameSpace(Session session, String pkgName, String pkgUrl) throws RepositoryException {
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
