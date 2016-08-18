package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.PermissionDao;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.dao.jcr.utils.JCRNodePropertyName;
import com.sp.dao.api.JCRIRepository;
import com.sp.model.*;
import com.sp.model.Privilege;
import com.sp.utils.Convert;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.*;
import org.apache.jackrabbit.commons.SimpleValueFactory;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.security.acl.PrincipalImpl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.security.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by pankajmishra on 17/08/16.
 */
@Repository(value = "permissionDao")
public class JCRPermissionDao implements PermissionDao<JCRUser> {


    @Autowired
    JCRUserDao jcrUserDao;

    @Autowired
    JCRIRepository JCRIRepository;


    @Override
    public void grant(Permission permission, JCRUser userObj) throws DatabaseException {
        grantPermissionToAuthorizable(permission, userObj.getUserName());
    }

    @Override
    public void revoke(Permission permission, JCRUser userObj) throws DatabaseException {
        revokePermissionToAuthorizable(permission, userObj.getUserName());
    }

    @Override
    public void grant(Permission permission, String groupName) throws DatabaseException {
        grantPermissionToAuthorizable(permission, groupName);
    }

    @Override
    public void revoke(Permission permission, String groupName) throws DatabaseException {
        revokePermissionToAuthorizable(permission, groupName);
    }

    @Override
    public void defineRole(Role role) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().
                    createGroup(role.getName());

            SimpleValueFactory simpleValueFactory = new SimpleValueFactory();
            for(Permission permission : role.getPermissions() ){


                if(authorizable.hasProperty(permission.getPrivilege().name())){
                    Value[] alreadyContainedValues = authorizable.getProperty(permission.getPrivilege().name());
                    boolean add = true;

                    for(Value alreadyContainedValue: alreadyContainedValues){
                        if(alreadyContainedValue.getString().equals((String) permission.getTarget().get__id())){
                            add = false;
                        }
                    }

                    if(add){
                        Value[] newArray = new Value[alreadyContainedValues.length + 1];
                        System.arraycopy(alreadyContainedValues,0, newArray, 0, alreadyContainedValues.length);
                        newArray[alreadyContainedValues.length] = simpleValueFactory .
                                createValue((String) permission.getTarget().get__id());
                        authorizable.setProperty(permission.getPrivilege().name(), newArray );
                    }

                } else {
                    authorizable.setProperty(permission.getPrivilege().name(), simpleValueFactory .
                            createValue((String) permission.getTarget().get__id())  );
                }

            }
            authorizable.setProperty(JCRNodePropertyName.ROLE_LINK_NAME, simpleValueFactory.createValue(true));
            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    //TODO : how do we make sure that this particular role is not defined to any user.
    @Override
    public void deleteRole(Role role) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().
                    getAuthorizable(role.getName());
            authorizable.remove();
            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public void assignRole(Role role, IUser userObj)  throws DatabaseException {
        assignRoleToAuthorizable(role, userObj.getUserName());
    }


    @Override
    public void assignRole(Role role, String group)  throws DatabaseException {
        assignRoleToAuthorizable(role, group);
    }

    @Override
    public void revokeRole(Role role, IUser userObj)  throws DatabaseException  {
        revokeRoleToAuthorizable(role, userObj.getUserName());
    }



    @Override
    public void revokeRole(Role role, String group)  throws DatabaseException  {
        revokeRoleToAuthorizable(role, group);
    }




    /**
     *
     */
    @Override
    public List<Role> getAllRole() throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Iterator<Authorizable> dbRoles = jackrabbitSession.getUserManager().
                    findAuthorizables (new Query() {
                        public <T> void build(QueryBuilder<T> builder) {
                            builder.setSelector(Group.class);
                            builder.setCondition(builder.exists(JCRNodePropertyName.ROLE_LINK_NAME));
                        }
                    });

            List<Role> result = new ArrayList<Role>();

            while (dbRoles.hasNext()){
                Role role = getRole(dbRoles);
                result.add(role);
            }
            session.save();

            return result;
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    private Role getRole(Iterator<Authorizable> dbRoles) throws RepositoryException {
        Role role = new Role();

        Authorizable dbRole = dbRoles.next();

        Iterator<String> propertyNames = dbRole.getPropertyNames();

        while (propertyNames.hasNext()){
            String propertyName = propertyNames.next();
            if(JCRNodePropertyName.ROLE_LINK_NAME.equals(propertyName)){
                continue;
            }
            Value[] values = dbRole.getProperty(propertyName);

            for(Value value : values){

                String pathName = value.getString();
                Permission permission = new Permission(new Identity() {
                    @Override
                    public Serializable get__id() {
                        return pathName;
                    }
                }, Privilege.valueOf(propertyName));

                role.getPermissions().add(permission);
            }
        }

        role.setName(dbRole.getID());
        return role;
    }


    @Override
    public  void updateRole(Role role) throws DatabaseException {
        Session session = null;
        try {

            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().
                    getAuthorizable(new PrincipalImpl(role.getName()));

            SimpleValueFactory simpleValueFactory = new SimpleValueFactory();
            for( Permission permission : role.getPermissions() ){
                if(authorizable.hasProperty(permission.getPrivilege().name())){
                    authorizable.removeProperty(permission.getPrivilege().name());
                } else {
                    authorizable.setProperty(permission.getPrivilege().name(),
                            simpleValueFactory.createValue((String)permission.getTarget().get__id()) );
                }
            }

            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    // helper functions


    /**
     * Its for both user and group
     * @param permission
     * @param authorizableId
     * @throws RepositoryException
     */
    private void grantPermissionToAuthorizable(Permission permission, String  authorizableId )  throws DatabaseException {

        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().getAuthorizable(authorizableId);
            String targetIdPath = (String) permission.getTarget().get__id();
            AccessControlManager accessControlManager = jackrabbitSession.getAccessControlManager();
            AccessControlList accessControlList =  addIntoAccessControlList( authorizable, accessControlManager, targetIdPath,
                    permission.getPrivilege());
            accessControlManager.setPolicy(targetIdPath, accessControlList);
            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    /**
     * Its for both user and group
     * @param permission
     * @param authorizableId
     * @throws RepositoryException
     */
    private void revokePermissionToAuthorizable(Permission permission, String authorizableId)  throws DatabaseException{

        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
        Authorizable authorizable = jackrabbitSession.getUserManager().getAuthorizable(authorizableId);
        String id = (String) permission.getTarget().get__id();
        AccessControlManager accessControlManager = jackrabbitSession.getAccessControlManager();
        AccessControlList accessControlList = removeFromAccessControlList( authorizable, accessControlManager, id,
                permission.getPrivilege());
        accessControlManager.setPolicy(id, accessControlList);
            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    /**
     *
     * TODO: Need to think how to give access controll for adding and removing users.
     * Convering our privilege set into JCR privilege set.
     * @param ourPrivilege
     * @param accessControlManager
     * @return
     * @throws RepositoryException
     */
    private  javax.jcr.security.Privilege[] getJCRPrivilegeFromOurPrivilege
    (Privilege ourPrivilege, AccessControlManager accessControlManager) throws RepositoryException {

        switch (ourPrivilege){
            case Executive:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(javax.jcr.security.Privilege.JCR_ALL) };
            case Write:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_WRITE
                                ),
                        // we need this because we want to make our node referenceable.
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_NODE_TYPE_MANAGEMENT
                        ),
                };
            case AddChild:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_ADD_CHILD_NODES
                        )
                };
            case RemoveChild:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_REMOVE_CHILD_NODES
                        )
                };
            case RemoveNode:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_REMOVE_NODE
                        ),
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_ADD_CHILD_NODES
                        ),
                };
            case Read:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_READ
                        )
                };
            case ModifyProperties:
                return new javax.jcr.security.Privilege[] {
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_MODIFY_PROPERTIES
                        )
                };
            case UserAndGroupAdd:
                return null;
            default:
                return null;
        }
    }



    /**
     *
     * @param user
     * @param accessControlManager
     * @param nodeAbsPath
     * @param cmsPrivilege
     * @return
     * @throws RepositoryException
     */
    private AccessControlList addIntoAccessControlList(Authorizable user, AccessControlManager accessControlManager,
                                                   String  nodeAbsPath , Privilege cmsPrivilege ) throws RepositoryException {
        // create a privilege set with jcr:all
        javax.jcr.security.Privilege[] privileges = getJCRPrivilegeFromOurPrivilege(cmsPrivilege, accessControlManager);
        AccessControlList acl = AccessControlUtils.getAccessControlList(accessControlManager, nodeAbsPath);

        AccessControlEntry aceForUser = null;

        List<javax.jcr.security.Privilege> exitingPrivileges = null;


        // search for existing ace for given user
        for(AccessControlEntry accessControlEntry : acl.getAccessControlEntries()){
            // this is existing ace for user.
            if(accessControlEntry.getPrincipal().equals(user.getPrincipal())){
                aceForUser = accessControlEntry;
                // diff of privilege that remain for given user.
                exitingPrivileges = Convert.fromArrayToList(accessControlEntry.getPrivileges());
            }
        }

        if(exitingPrivileges == null){
            acl.addAccessControlEntry(user.getPrincipal(), privileges);
        } else {
            for(javax.jcr.security.Privilege privilege : privileges){
                if(!exitingPrivileges.contains(privilege)){
                    exitingPrivileges.add(privilege);
                }
            }
            acl.addAccessControlEntry(user.getPrincipal(), exitingPrivileges.toArray(new javax.jcr.security.Privilege[0]));
        }
        return acl;
    }


    /**
     *
     * @param user
     * @param accessControlManager
     * @param nodeAbsPath
     * @param privilege
     * @return
     * @throws RepositoryException
     */
    private AccessControlList removeFromAccessControlList(Authorizable user, AccessControlManager accessControlManager,
                                                       String  nodeAbsPath , Privilege privilege ) throws RepositoryException {
        // create a privilege set with jcr:all
        javax.jcr.security.Privilege[] privileges = getJCRPrivilegeFromOurPrivilege(privilege, accessControlManager);

        JackrabbitAccessControlList acl =  AccessControlUtils.getAccessControlList(accessControlManager, nodeAbsPath);



        AccessControlEntry aceForUser = null;
        List<javax.jcr.security.Privilege> remainingPrivileges = null;


        // search for existing ace for given user
        for(AccessControlEntry accessControlEntry : acl. getAccessControlEntries()){
            // this is existing ace for user.
            if(accessControlEntry.getPrincipal().equals(user.getPrincipal())){
                aceForUser = accessControlEntry;
               // diff of privilege that remain for given user.
                remainingPrivileges = new ArrayList<>();

                for(javax.jcr.security.Privilege existingPrivilege : accessControlEntry.getPrivileges()){

                    boolean contains = false;
                    for(javax.jcr.security.Privilege removingPrivilege : privileges){
                        if(existingPrivilege.equals(removingPrivilege)){
                            contains = true;
                        }
                    }
                    if(!contains)
                       remainingPrivileges.add(existingPrivilege);
                }
            }
        }

        if(aceForUser != null){
            // remove this access control.
            acl.removeAccessControlEntry(aceForUser);
        }
        // if diff remains then add remaining
        if(remainingPrivileges != null && remainingPrivileges.size() > 0){
            acl.addAccessControlEntry(user.getPrincipal(), remainingPrivileges.toArray(new javax.jcr.security.Privilege[0]));
        }
        acl.addEntry(user.getPrincipal(), privileges, false);
        return acl;
    }


    private void assignRoleToAuthorizable(Role role, String authorizableId) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().
                    getAuthorizable(role.getName());

            Iterator<String> propertyNames = authorizable.getPropertyNames();

            while (propertyNames.hasNext()){
                List<Permission> permissions = getPermission(authorizable, propertyNames);
                if(permissions != null){
                    for(Permission permission : permissions) {
                        grantPermissionToAuthorizable(permission, authorizableId);
                    }
                }

            }

            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    private List<Permission> getPermission(Authorizable authorizable, Iterator<String> propertyNames) throws RepositoryException {

        List<Permission> permissions = new ArrayList<>();
        String propertyName = propertyNames.next();
        if(JCRNodePropertyName.ROLE_LINK_NAME.equals(propertyName)){
            return null;
        }
        Value[] values = authorizable.getProperty(propertyName);

        for(Value value : values){
            final String pathName = value.getString();

            permissions.add(new Permission(new Identity() {
                @Override
                public Serializable get__id() {
                    return pathName;
                }
            }, Privilege.valueOf(propertyName)) );
        }

        return permissions;
    }


    private void revokeRoleToAuthorizable(Role role, String authorizableId) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession =(JackrabbitSession)session;
            Authorizable authorizable = jackrabbitSession.getUserManager().
                    getAuthorizable(new PrincipalImpl(role.getName()));

            Iterator<String> propertyNames = authorizable.getPropertyNames();

            while (propertyNames.hasNext()){
                List<Permission> permissions = getPermission(authorizable, propertyNames);
                if(permissions != null) {
                    for(Permission permission : permissions) {
                        revokePermissionToAuthorizable(permission, authorizableId);
                    }
                }
            }
            session.save();
        } catch (Exception e){
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }


}
