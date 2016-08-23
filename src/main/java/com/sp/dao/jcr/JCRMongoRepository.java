package com.sp.dao.jcr;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DatabaseInitException;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;

import javax.annotation.PreDestroy;

/**
 * Created by pankajmishra on 07/08/16.
 */
//@org.springframework.stereotype.Repository
public class JCRMongoRepository  extends JCRAbstractRepository {


    private static final String MONGO_SERVER_IP = "127.0.0.1";

    private static final String MONGO_DATABASE_NAME = "cms";

    private DocumentNodeStore dns = null;

    @Override
    protected void initializeRepo() throws DatabaseException {

        try {
            DB db = new Mongo(MONGO_SERVER_IP, 27017).getDB(MONGO_DATABASE_NAME);
            dns = new DocumentMK.Builder().
                    setMongoDB(db).getNodeStore();
            repository = new Jcr(new Oak(dns)).createRepository();
        } catch (Exception e){
            throw new DatabaseInitException(e);
        }
    }

    @Override
    @PreDestroy
    public void shutDown() {
        if (dns != null) {
            //  dns.dispose();
        }
    }

}
