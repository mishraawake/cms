package com.sp.dao.jcr;

import com.google.common.collect.Lists;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.api.UserDao;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.dao.jcr.utils.UserDaoUtils;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pankajmishra on 15/08/16.
 */
@Repository(value = "userDao")
public class JCRUserDao implements UserDao<JCRUser> {

    @Autowired
    StringSerialization serialization;


    @Autowired
    JCRIRepository jcrIRepository;

    /**
     * Because jcr in itself does not provide user management so we need to user jackrabbit API for this.
     *
     * @param userObj
     * @return
     * @throws DatabaseException
     */
    @Override
    public JCRUser create(JCRUser userObj) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();

            Authorizable user = session.getUserManager().createUser(
                    JcrNameFac.getUserMgmtName(userObj.getUserName()).name(), String.valueOf(userObj
                    .getPassword()));

            Node fieldNode = createFieldNodeForUser(userObj.getIdentityForPath(), userObj.getProperties(), session);

            ValueFactory valueFactory = session.getValueFactory();
            Value fieldValue = valueFactory.createValue(fieldNode.getPath());
            UserDaoUtils.setProperty(user, FixedNames.fields(), fieldValue );
            UserDaoUtils.setProperty(user, FixedNames.name(),  valueFactory.createValue(userObj.getUserName()));
            session.save();
            return userObj;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * Each user will have a field that will contain all their properties.
     * @param userName
     * @param properties
     * @param session
     * @return
     * @throws Exception
     */
    private Node createFieldNodeForUser(String userName, List<FieldValue> properties, Session session) throws
            Exception {
        Node userNode = null;
        userNode = JcrDaoUtils.createIfNotExist(session.getRootNode() , FixedNames.user() );
        Node fieldNode = JcrDaoUtils.addNode(userNode, JcrNameFac.getUserName(userName));
        JcrDaoUtils.populateNodeFromItem(properties, fieldNode, session, serialization);
        return fieldNode;
    }

    @Override
    public JCRUser createOrUpdate(JCRUser element) throws DatabaseException {
        return null;
    }

    @Override
    public JCRUser update(JCRUser element) throws DatabaseException {
        return null;
    }

    @Override
    public JCRUser get(Serializable id) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName((String)id));
            return getUserFromAuthorizable(user, session);
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * Helper method to get user object from jcr representation of user.
     *
     * @param user
     * @param session
     * @return
     * @throws Exception
     */
    private JCRUser getUserFromAuthorizable(Authorizable user, Session session) throws Exception {

        JCRUser userObj = new JCRUser();
        userObj.setUserName( UserDaoUtils.getProperty(user, FixedNames.name())[0].getString() );
        Value userFieldValue = UserDaoUtils.getProperty(user, FixedNames.fields())[0];
        String userFieldPath = userFieldValue.getString();
        Node userFieldNode = session.getNode(userFieldPath);
        List<FieldValue> properties = JcrDaoUtils.getFieldsFromNode(userFieldNode, serialization);
        userObj.setProperties(properties);
        return userObj;
    }


    private Iterator<JCRUser> getUserIterator(final int pageSize){

        Iterator<JCRUser> iterator = new Iterator<JCRUser>() {

            private int offset = 0;
            private int limit = pageSize;
            private boolean available = true;
            JackrabbitSession session = null;
            Iterator<Authorizable> authorizableIterable = null;

            private Iterator<Authorizable> getIterator() throws Exception {
                if(session != null){
                    session.logout();
                }
                session = (JackrabbitSession)jcrIRepository.getSession();

                final Iterator<Authorizable> authorizableIterable = session.getUserManager().findAuthorizables(new Query() {

                    @Override
                    public <T> void build(QueryBuilder<T> builder) {
                        builder.setSelector(User.class);
                        builder.setCondition( builder.exists(FixedNames.name().name()));
                        builder.setLimit(offset, limit);
                    }
                });
                return authorizableIterable;
            }


            @Override
            public boolean hasNext() {
                try {
                    if (authorizableIterable == null) {
                        authorizableIterable = getIterator();
                    }
                    if (authorizableIterable.hasNext()) {
                        return true;
                    } else if( available ) {
                        offset += limit;
                        authorizableIterable = getIterator();
                    } else {
                        return false;
                    }
                    if(authorizableIterable.hasNext()){
                        return true;
                    } else {
                        available = false;
                    }
                    return available;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public JCRUser next() {
                try {
                    return getUserFromAuthorizable(authorizableIterable.next(), session);
                } catch (Exception e) {
                    throw  new RuntimeException(e);
                }
            }
        };

        return iterator;
    }

    private static class QueryImpl implements Query {
        QueryBuilder builder = null;
        @Override
        public <T> void build(QueryBuilder<T> builder) {
            this.builder = builder;
        }
    }
    //TODO : have to implement paging.
    // assume default paging is 10.
    @Override
    public Iterable<JCRUser> list() throws DatabaseException {
            return () -> {
                return getUserIterator(3);
            };
    }

    @Override
    public Long count() throws DatabaseException {
        return null;
    }

    @Override
    public void delete(Serializable id) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName((String)id));
            user.remove();
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public boolean exists(Serializable id) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName((String)id));
            return user != null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public void createGroup(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable authorizable = UserDaoUtils.createGroup(session, JcrNameFac.getGroupName(groupName));
            UserDaoUtils.setProperty(authorizable, FixedNames.name(), session.getValueFactory().createValue(groupName));
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public boolean groupExists(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Group group = (Group)UserDaoUtils.getAuthorizable(session, JcrNameFac.getGroupName(groupName));
            return group != null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public void removeGroup(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable group = UserDaoUtils.getAuthorizable(session, JcrNameFac.getGroupName(groupName));
            group.remove();
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    //TODO : have to implement paging.
    @Override
    public CloseableIterator<JCRUser> getMemberOf(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            final Session innerSession = session;
            Group group = (Group)UserDaoUtils.getAuthorizable (session, JcrNameFac.getGroupName(groupName));
            final Iterator<Authorizable> authorizableIterable = group.getDeclaredMembers();

            return new CloseableIterator<JCRUser>() {
                @Override
                public void close() {
                    if(innerSession != null){
                        innerSession.logout();
                    }
                }

                @Override
                public boolean hasNext() {
                    return authorizableIterable.hasNext();
                }

                @Override
                public JCRUser next() {
                    try {
                        return getUserFromAuthorizable(authorizableIterable.next(), innerSession);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("problem in converting authority object into user " +
                                "object. "));
                    }
                }
            };

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {

        }
    }

    @Override
    public List<String> getGroupOf(String userName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Authorizable user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName(userName));
            List<String> groupNames = new ArrayList<>();
            Iterator<Group> groups = user.declaredMemberOf();
            while (groups.hasNext()) {
                groupNames.add(UserDaoUtils.getProperty(groups.next(), FixedNames.name())[0].getString());
            }
            session.save();
            return groupNames;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public void addUserToGroup(JCRUser userObj, String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            Group group = (Group) UserDaoUtils.getAuthorizable(session, JcrNameFac.getGroupName(groupName));
            if (group == null) {
                createGroup(groupName);
                group = (Group) UserDaoUtils.getAuthorizable(session, JcrNameFac.getGroupName(groupName));
            }

            Authorizable user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName(userObj.getUserName()));
            if (user == null) {
                userObj = create(userObj);
                user = UserDaoUtils.getAuthorizable(session, JcrNameFac.getUserMgmtName(userObj.getUserName()));
            }
            group.addMember(user);
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public List<String> getAllGroup() throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) jcrIRepository.getSession();
            final Iterator<Authorizable> authorizableIterable = session.getUserManager().findAuthorizables(new Query() {
                public <T> void build(QueryBuilder<T> builder) {
                    builder.setSelector(Group.class);
                    builder.setCondition( builder.not( builder.exists(FixedNames.role().name()) ));
                }
            });

            Iterator<String> iterator = new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return authorizableIterable.hasNext();
                }

                @Override
                public String next() {
                    try {
                        return UserDaoUtils.getProperty(authorizableIterable.next(), FixedNames.name())[0].getString();
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("problem in converting authority object into user " +
                                "object. "), e);
                    }
                }
            };

            return Lists.newArrayList(iterator);

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

}
