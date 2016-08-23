package com.sp.dao.jcr.utils;

import com.sp.dao.jcr.model.JcrName;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;

import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Created by pankajmishra on 21/08/16.
 */
public class UserDaoUtils {

    public static void setProperty(Authorizable authorizable, JcrName name, Value value) throws RepositoryException {
        authorizable.setProperty(name.name(), value );
    }

    public static void setProperty(Authorizable authorizable, JcrName name, Value[] values) throws RepositoryException {
        authorizable.setProperty(name.name(), values );
    }

    public static Value[] getProperty(Authorizable authorizable, JcrName name) throws RepositoryException {
        if(!authorizable.hasProperty(name.name())){
            return new Value[0];
        }
        return authorizable.getProperty(name.name());
    }

    public static Authorizable getAuthorizable(JackrabbitSession session, JcrName name) throws
            RepositoryException {
        return session.getUserManager().getAuthorizable(name.name());
    }

    public static Group createGroup(JackrabbitSession session, JcrName name) throws
            RepositoryException {
         return session.getUserManager().createGroup(name.name());
    }
}
