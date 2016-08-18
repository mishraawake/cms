package com.sp.dao.jcr;

import com.google.common.collect.Lists;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.UserDao;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.dao.jcr.utils.JCRNodePropertyName;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import com.sp.utils.NameUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.*;
import org.apache.jackrabbit.commons.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Value;
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
    JCRIRepository JCRIRepository;

    /**
     * Because jcr in itself does not provide user management so we need to user jackrabbit API for this.
     * @param userObj
     * @return
     * @throws DatabaseException
     */
    @Override
    public JCRUser create(JCRUser userObj) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable user = session.getUserManager().createUser(userObj.getUserName(), String.valueOf(userObj.getPassword()));
            Node fieldNode = createFieldNodeForUser(userObj.getIdentityForPath(), userObj.getProperties(), session);
            SimpleValueFactory simpleValueFactory = new SimpleValueFactory();
            Value fieldValue =  simpleValueFactory.createValue(fieldNode.getPath());
            user.setProperty(JCRNodePropertyName.FIELDS_LINK_NAME, fieldValue);
            session.save();
            return userObj;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }


    private Node createFieldNodeForUser(String userName, List<FieldValue> properties, Session session) throws Exception {
        Node userNode = null;
        if(session.getRootNode().hasNode(JCRNodePropertyName.USER_LINK_VALUE )){
            userNode =  session.getRootNode().getNode(JCRNodePropertyName.USER_LINK_VALUE );
        } else {
            userNode =  session.getRootNode().addNode(JCRNodePropertyName.USER_LINK_VALUE );
        }
        Node fieldNode = userNode.addNode(userName);
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
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable user = session.getUserManager().getAuthorizable((String)id);
            return getUserFromAuthorizable(user, session);
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    /**
     * Helper method to get user object from jcr representation of user.
     * @param user
     * @param session
     * @return
     * @throws Exception
     */
    private JCRUser getUserFromAuthorizable(Authorizable user, Session session) throws Exception {

        JCRUser userObj = new JCRUser();
        userObj.setUserName(user.getID());
        Value userFieldValue = user.getProperty(JCRNodePropertyName.FIELDS_LINK_NAME)[0];
        String userFieldPath = userFieldValue.getString();
        Node userFieldNode = session.getNode(userFieldPath);
        List<FieldValue> properties = JcrDaoUtils.getFieldsFromNode(userFieldNode, serialization);
        userObj.setProperties( properties);
        return userObj;
    }

    //TODO : have to implement paging.
    @Override
    public Iterable<JCRUser> list() throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            final Iterator<Authorizable> authorizableIterable = session.getUserManager().findAuthorizables(new Query() {
                public <T> void build(QueryBuilder<T> builder) {
                    builder.setSelector(User.class);
                    builder.setCondition(builder.exists(JCRNodePropertyName.FIELDS_LINK_NAME));
                }
            });

            Iterator<JCRUser> iterator =  new Iterator<JCRUser>() {
                @Override
                public boolean hasNext() {
                    return authorizableIterable.hasNext();
                }

                @Override
                public JCRUser next() {
                    Session innerSession = null;
                    try {
                        innerSession = JCRIRepository.getSession();
                        return getUserFromAuthorizable(authorizableIterable.next(), innerSession);
                    } catch (Exception e) {
                        throw  new RuntimeException(String.format("problem in converting authority object into user " +
                                "object. "), e);
                    } finally {
                        if(innerSession != null)
                            innerSession.logout();
                    }
                }
            };

            return new Iterable<JCRUser>() {
                @Override
                public Iterator<JCRUser> iterator() {
                    return iterator;
                }
            };
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
           /* if(session != null){
                session.logout();
            } */
        }
    }

    @Override
    public Long count() throws DatabaseException {
        return null;
    }

    @Override
    public void delete(Serializable id) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable user = session.getUserManager().getAuthorizable((String)id);
            user.remove();
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public boolean exists(Serializable id) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable user = session.getUserManager().getAuthorizable((String)id);
            return user != null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public void createGroup(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            session.getUserManager().createGroup(NameUtils.getJCRLikeName(groupName));
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public boolean groupExists(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Group group = (Group)session.getUserManager().getAuthorizable(groupName);
            session.save();
            return group != null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public void removeGroup(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable group = session.getUserManager().getAuthorizable(NameUtils.getJCRLikeName(groupName));
            group.remove();
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    //TODO : have to implement paging.
    @Override
    public Iterator<JCRUser> getMemberOf(String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Group group = (Group)session.getUserManager().getAuthorizable(groupName);
            final Iterator<Authorizable> authorizableIterable = group.getDeclaredMembers();

            return new Iterator<JCRUser>() {
                @Override
                public boolean hasNext() {
                    return authorizableIterable.hasNext();
                }

                @Override
                public JCRUser next() {
                    Session innerSession = null;
                    try {
                        innerSession = JCRIRepository.getSession();
                        return getUserFromAuthorizable(authorizableIterable.next(), innerSession);
                    } catch (Exception e) {
                       throw  new RuntimeException(String.format("problem in converting authority object into user " +
                               "object. "));
                    } finally {
                        if(innerSession != null)
                            innerSession.logout();
                    }
                }
            };

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
          /*  if(session != null){
                session.logout();
            } */
        }
    }

    @Override
    public List<String> getGroupOf(String userName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Authorizable user = session.getUserManager().getAuthorizable(userName);
            List<String> groupNames = new ArrayList<String>();
            Iterator<Group> groups = user.declaredMemberOf();
            while (groups.hasNext()){
                groupNames.add(groups.next().getID());
            }
            session.save();
            return groupNames;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public void addUserToGroup(JCRUser userObj, String groupName) throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            Group group = (Group)session.getUserManager().getAuthorizable(groupName);
            if(group == null){
                group = (Group)session.getUserManager().createGroup(groupName);
            }

            Authorizable user = session.getUserManager().getAuthorizable(userObj.getUserName());
            if(user == null){
                userObj = create(userObj);
                user = session.getUserManager().getAuthorizable(userObj.getUserName());
            }
            group.addMember(user);
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public List<String> getAllGroup() throws DatabaseException {
        JackrabbitSession session = null;
        try {
            session = (JackrabbitSession) JCRIRepository.getSession();
            final Iterator<Authorizable> authorizableIterable = session.getUserManager().findAuthorizables(new Query() {
                public <T> void build(QueryBuilder<T> builder) {
                    builder.setSelector(Group.class);
                }
            });

            Iterator<String> iterator =  new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return authorizableIterable.hasNext();
                }

                @Override
                public String next() {
                    try {
                        return authorizableIterable.next().getID();
                    } catch (Exception e) {
                        throw  new RuntimeException(String.format("problem in converting authority object into user " +
                                "object. "), e);
                    }
                }
            };

            return Lists.newArrayList(iterator);

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
           if(session != null){
                session.logout();
            }
        }
    }

}
