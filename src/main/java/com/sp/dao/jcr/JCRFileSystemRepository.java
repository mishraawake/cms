package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DatabaseInitException;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.segment.SegmentNodeStore;
import org.apache.jackrabbit.oak.plugins.segment.file.FileStore;

import java.io.File;

/**
 * Created by pankajmishra on 07/08/16.
 */
//@org.springframework.stereotype.Repository
public class JCRFileSystemRepository extends JCRAbstractRepository {

    final static int MAX_FILE_SIZE = 2^31;

    final static String DATABASE_LOCATION = "../repository";

    @Override
    protected void initializeRepo() throws DatabaseException {
        try {
            if (repository == null) {
                FileStore fs = new FileStore(new File(DATABASE_LOCATION), MAX_FILE_SIZE);
                repository = new Jcr(new SegmentNodeStore(fs)).createRepository();
            }
        } catch (Exception e){
            throw new DatabaseInitException(e);
        }
    }

    @Override
    public void shutDown() {

    }

}
