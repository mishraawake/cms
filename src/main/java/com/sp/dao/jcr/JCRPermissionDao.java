package com.sp.dao.jcr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.api.PermissionDao;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.dao.jcr.model.JcrName;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.UserDaoUtils;
import com.sp.model.IUser;
import com.sp.model.Permission;
import com.sp.model.Privilege;
import com.sp.model.Role;
import com.sp.service.StringSerialization;
import com.sp.utils.Convert;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.Query;
import org.apache.jackrabbit.api.security.user.QueryBuilder;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pankajmishra on 17/08/16.
 */
@Repository(value = "permissionDao")
public class JCRPermissionDao implements PermissionDao<JCRUser> {


    @Autowired
    JCRIRepository JCRIRepository;

    @Autowired
    StringSerialization stringSerialization;


    @Override
    public void grant(Permission permission, JCRUser userObj) throws DatabaseException {
        grantPermissionToAuthorizable(permission, JcrNameFac.getUserMgmtName(userObj.getUserName()));
    }

    @Override
    public void revoke(Permission permission, JCRUser userObj) throws DatabaseException {
        revokePermissionToAuthorizable(permission, JcrNameFac.getUserMgmtName(userObj.getUserName()));
    }

    @Override
    public void grant(Permission permission, String groupName) throws DatabaseException {
        grantPermissionToAuthorizable(permission, JcrNameFac.getGroupName(groupName));
    }

    @Override
    public void revoke(Permission permission, String groupName) throws DatabaseException {
        revokePermissionToAuthorizable(permission, JcrNameFac.getGroupName(groupName) );
    }

    @Override
    public void defineRole(Role role) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            // here we are saving role as a group with the name space provided by JcrNameFac.getRoleName
            Authorizable authorizable = UserDaoUtils.createGroup(jackrabbitSession, JcrNameFac.getRoleName(role
                    .getName()));

            ValueFactory valueFactory = session.getValueFactory();

            // going to save every permission. Each permission has one target items id and privilege on that items.
            updateRolePermission(role, authorizable, valueFactory);
            UserDaoUtils.setProperty(authorizable, FixedNames.role(), valueFactory.createValue(true));
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private void updateRolePermission(Role role, Authorizable roleAuthorizable, ValueFactory valueFactory) throws RepositoryException, IOException {
        for (Permission permission : role.getPermissions()) {

            Value[] alreadyContainedValues = UserDaoUtils.getProperty(roleAuthorizable,
                    JcrNameFac.getRoleName(permission.getPrivilege().name()));


            boolean add = true;
            for (Value alreadyContainedValue : alreadyContainedValues) {
                Permission dbPermission = getPermissionFromValue(alreadyContainedValue);
                if(dbPermission.equals(permission)){
                    add = false;
                }
            }

            if (add) {
                Value[] modifiedPermissions = new Value[alreadyContainedValues.length + 1];
                System.arraycopy(alreadyContainedValues, 0, modifiedPermissions, 0, alreadyContainedValues.length);

                modifiedPermissions[alreadyContainedValues.length] = valueFactory.
                        createValue(stringSerialization.serialize(permission));

                UserDaoUtils.setProperty(roleAuthorizable, JcrNameFac.getRoleName(permission.getPrivilege().name()),
                        modifiedPermissions);
            }
        }
    }


    private Permission getPermissionFromValue(Value value) throws RepositoryException, IOException {
        return stringSerialization.deserialize(value.getString(), Permission.class);
    }

    private Value getValueFromPermission(Permission value){
        return null;
    }

    //TODO : how do we make sure that this particular role is not defined to any user.
    @Override
    public void deleteRole(Role role) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable authorizable = UserDaoUtils.
                    getAuthorizable(jackrabbitSession, JcrNameFac.getGroupName(role.getName()));
            authorizable.remove();
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
    public void assignRole(Role role, IUser userObj) throws DatabaseException {
        assignRoleToAuthorizable(role, JcrNameFac.getUserMgmtName(userObj.getUserName()));
    }


    @Override
    public void assignRole(Role role, String group) throws DatabaseException {
        assignRoleToAuthorizable(role, JcrNameFac.getRoleName(group));
    }

    @Override
    public void revokeRole(Role role, IUser userObj) throws DatabaseException {
        revokeRoleToAuthorizable(role, JcrNameFac.getUserMgmtName(userObj.getUserName()));
    }


    @Override
    public void revokeRole(Role role, String group) throws DatabaseException {
        revokeRoleToAuthorizable(role, JcrNameFac.getRoleName(group) );
    }


    /**
     *
     */
    @Override
    public List<Role> getAllRole() throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Iterator<Authorizable> dbRoles = jackrabbitSession.getUserManager().
                    findAuthorizables(new Query() {
                        public <T> void build(QueryBuilder<T> builder) {
                            builder.setSelector(Group.class);
                            builder.exists(FixedNames.role().name());
                        }
                    });

            List<Role> result = new ArrayList<Role>();

            while (dbRoles.hasNext()) {
                Role role = getRole(dbRoles.next());
                result.add(role);
            }
            session.save();

            return result;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private Role getRole(Authorizable dbRole) throws RepositoryException, IOException {
        Role role = new Role();

        Iterator<String> propertyNames = dbRole.getPropertyNames();

        while (propertyNames.hasNext()) {
            String propertyName = propertyNames.next();
            // special sentinel properties for role group.
            if (FixedNames.role().name().equals(propertyName)) {
                continue;
            } else if(FixedNames.name().name().equals(propertyName)){
                role.setName(dbRole.getProperty(propertyName).toString());
            } else {
                Value[] values = dbRole.getProperty(propertyName);
                for (Value value : values) {
                    String permissionStr = value.getString();
                    Permission permission = stringSerialization.deserialize(permissionStr, Permission.class);
                    role.getPermissions().add(permission);
                }
            }
        }

        return role;
    }


    @Override
    public void updateRole(Role role) throws DatabaseException {
        Session session = null;
        try {

            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable roleAuthorizable = UserDaoUtils.getAuthorizable(jackrabbitSession,
                    JcrNameFac.getRoleName(role.getName()) );
            ValueFactory valueFactory = session.getValueFactory();
            updateRolePermission(role, roleAuthorizable, valueFactory);
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    // helper functions


    /**
     * Its for both user and group
     *
     * @param permission
     * @throws RepositoryException
     */
    private void grantPermissionToAuthorizable(Permission permission, JcrName name) throws DatabaseException {

        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable authorizable = UserDaoUtils.getAuthorizable(jackrabbitSession, name);
            String targetIdPath = (String) permission.getTargetId();
            AccessControlManager accessControlManager = jackrabbitSession.getAccessControlManager();
            AccessControlList accessControlList = addIntoAccessControlList(authorizable, accessControlManager,
                    targetIdPath,
                    permission.getPrivilege());
            accessControlManager.setPolicy(targetIdPath, accessControlList);
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * Its for both user and group
     *
     * @param permission
     * @throws RepositoryException
     */
    private void revokePermissionToAuthorizable(Permission permission,  JcrName name) throws DatabaseException {

        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable authorizable = UserDaoUtils.getAuthorizable(jackrabbitSession, name);
            String id = (String) permission.getTargetId();
            AccessControlManager accessControlManager = jackrabbitSession.getAccessControlManager();
            AccessControlList accessControlList = removeFromAccessControlList(authorizable, accessControlManager, id,
                    permission.getPrivilege());
            accessControlManager.setPolicy(id, accessControlList);
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * TODO: Need to think how to give access controll for adding and removing users.
     * Convering our privilege set into JCR privilege set.
     *
     * @param ourPrivilege
     * @param accessControlManager
     * @return
     * @throws RepositoryException
     */
    private javax.jcr.security.Privilege[] getJCRPrivilegeFromOurPrivilege
    (Privilege ourPrivilege, AccessControlManager accessControlManager) throws RepositoryException {

        switch (ourPrivilege) {
            case Executive:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(javax.jcr.security.Privilege.JCR_ALL)};
            case Write:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_WRITE
                        ),
                        // we need this because we want to make our node referenceable.
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_NODE_TYPE_MANAGEMENT
                        ),
                };
            case AddChild:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_ADD_CHILD_NODES
                        )
                };
            case RemoveChild:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_REMOVE_CHILD_NODES
                        )
                };
            case RemoveNode:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_REMOVE_NODE
                        ),
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_ADD_CHILD_NODES
                        ),
                };
            case Read:
                return new javax.jcr.security.Privilege[]{
                        accessControlManager.privilegeFromName(
                                javax.jcr.security.Privilege.JCR_READ
                        )
                };
            case ModifyProperties:
                return new javax.jcr.security.Privilege[]{
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
     * @param user
     * @param accessControlManager
     * @param nodeAbsPath
     * @param cmsPrivilege
     * @return
     * @throws RepositoryException
     */
    private AccessControlList addIntoAccessControlList(Authorizable user, AccessControlManager accessControlManager,
                                                       String nodeAbsPath, Privilege cmsPrivilege) throws
            RepositoryException {
        // create a privilege set with jcr:all
        javax.jcr.security.Privilege[] privileges = getJCRPrivilegeFromOurPrivilege(cmsPrivilege, accessControlManager);
        AccessControlList acl = AccessControlUtils.getAccessControlList(accessControlManager, nodeAbsPath);

        AccessControlEntry aceForUser = null;

        List<javax.jcr.security.Privilege> exitingPrivileges = null;


        // search for existing ace for given user
        for (AccessControlEntry accessControlEntry : acl.getAccessControlEntries()) {
            // this is existing ace for user.
            if (accessControlEntry.getPrincipal().equals(user.getPrincipal())) {
                aceForUser = accessControlEntry;
                // diff of privilege that remain for given user.
                exitingPrivileges = Convert.fromArrayToList(accessControlEntry.getPrivileges());
            }
        }

        if (exitingPrivileges == null) {
            acl.addAccessControlEntry(user.getPrincipal(), privileges);
        } else {
            for (javax.jcr.security.Privilege privilege : privileges) {
                if (!exitingPrivileges.contains(privilege)) {
                    exitingPrivileges.add(privilege);
                }
            }
            acl.addAccessControlEntry(user.getPrincipal(), exitingPrivileges.toArray(new javax.jcr.security
                    .Privilege[0]));
        }
        return acl;
    }


    /**
     * @param user
     * @param accessControlManager
     * @param nodeAbsPath
     * @param privilege
     * @return
     * @throws RepositoryException
     */
    private AccessControlList removeFromAccessControlList(Authorizable user, AccessControlManager accessControlManager,
                                                          String nodeAbsPath, Privilege privilege) throws
            RepositoryException {
        // create a privilege set with jcr:all
        javax.jcr.security.Privilege[] privileges = getJCRPrivilegeFromOurPrivilege(privilege, accessControlManager);

        JackrabbitAccessControlList acl = AccessControlUtils.getAccessControlList(accessControlManager, nodeAbsPath);


        AccessControlEntry aceForUser = null;
        List<javax.jcr.security.Privilege> remainingPrivileges = null;


        // search for existing ace for given user
        for (AccessControlEntry accessControlEntry : acl.getAccessControlEntries()) {
            // this is existing ace for user.
            if (accessControlEntry.getPrincipal().equals(user.getPrincipal())) {
                aceForUser = accessControlEntry;
                // diff of privilege that remain for given user.
                remainingPrivileges = new ArrayList<>();

                for (javax.jcr.security.Privilege existingPrivilege : accessControlEntry.getPrivileges()) {

                    boolean contains = false;
                    for (javax.jcr.security.Privilege removingPrivilege : privileges) {
                        if (existingPrivilege.equals(removingPrivilege)) {
                            contains = true;
                        }
                    }
                    if (!contains)
                        remainingPrivileges.add(existingPrivilege);
                }
            }
        }

        if (aceForUser != null) {
            // remove this access control.
            acl.removeAccessControlEntry(aceForUser);
        }
        // if diff remains then add remaining
        if (remainingPrivileges != null && remainingPrivileges.size() > 0) {
            acl.addAccessControlEntry(user.getPrincipal(), remainingPrivileges.toArray(new javax.jcr.security
                    .Privilege[0]));
        }
        acl.addEntry(user.getPrincipal(), privileges, false);
        return acl;
    }


    private void assignRoleToAuthorizable(Role role, JcrName userName) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable roleAuthorizable =
                    UserDaoUtils.getAuthorizable(jackrabbitSession, JcrNameFac.getRoleName(role.getName()));

            Iterator<String> propertyNames = roleAuthorizable.getPropertyNames();

            while (propertyNames.hasNext()) {
                List<Permission> permissions = getPermission(roleAuthorizable, propertyNames);
                if (permissions != null) {
                    for (Permission permission : permissions) {
                        grantPermissionToAuthorizable(permission, userName);
                    }
                }
            }
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private List<Permission> getPermission(Authorizable roleAuthorizable, Iterator<String> propertyNames) throws
            RepositoryException, IOException {

        List<Permission> permissions = new ArrayList<>();
        String propertyName = propertyNames.next();
        if (FixedNames.role().name().equals(propertyName)) {
            return null;
        }

        Value[] values = roleAuthorizable.getProperty(propertyName);

        for (Value value : values) {
            permissions.add(getPermissionFromValue(value));
        }
        return permissions;
    }


    private void revokeRoleToAuthorizable(Role role, JcrName jcrName) throws DatabaseException {
        Session session = null;
        try {
            session = JCRIRepository.getSession();
            JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
            Authorizable roleAuthorizable =
                    UserDaoUtils.getAuthorizable(jackrabbitSession, JcrNameFac.getRoleName(role.getName()));

            Iterator<String> propertyNames = roleAuthorizable.getPropertyNames();

            while (propertyNames.hasNext()) {
                List<Permission> permissions = getPermission(roleAuthorizable, propertyNames);
                if (permissions != null) {
                    for (Permission permission : permissions) {
                        revokePermissionToAuthorizable(permission, jcrName);
                    }
                }
            }
            session.save();
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }




}
