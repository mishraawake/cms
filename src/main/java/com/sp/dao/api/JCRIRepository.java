package com.sp.dao.api;

import com.sp.model.IUser;
import com.sp.service.SubjectProvider;
import com.sp.service.impl.SubjectProviderFactory;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jcr.*;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface JCRIRepository {

    public static final String MY_NAME_SPACE_PREFIX = "sp";
    public static final String PACKAGE_NAME_SPACE_PREFIX = "pkg";
    public static final String MY_NAME_SPACE_URL = "sp.com";
    public static final String PACKAGE_NAME_SPACE_URL = "sp.com/pkg";

    public Session getSession() throws RepositoryException ;

}
