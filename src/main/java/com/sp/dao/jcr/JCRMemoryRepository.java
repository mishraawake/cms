package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseInitException;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository
public class JCRMemoryRepository extends JCRAbstractRepository {

    @Override
    protected void initializeRepo() throws DatabaseInitException {
        try {
            if (repository == null) {
                repository = new Jcr(new Oak()).createRepository();
            }
        } catch (Exception e){
            throw new DatabaseInitException(e);
        }
    }

    @Override
    public void shutDown() {

    }


}
