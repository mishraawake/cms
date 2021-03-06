package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.PermissionDao;
import com.sp.dao.api.SPPermissionDao;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.model.Permission;
import com.sp.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by pankajmishra on 18/08/16.
 */
@Repository(value = "spPermissionDao")
public class JCRSPPermissionDao implements SPPermissionDao<JCRUser> {

    @Autowired
    PermissionDao<JCRUser> jcrPermissionDao;

    @Autowired
    JCRItemDao jcrItemDao;

    @Override
    public void grant(Permission permission, JCRUser user) throws DatabaseException {

        JCRItem item = jcrItemDao.get(permission.getTargetId());
        jcrPermissionDao.grant(permission, user);
        JCRDefinition jcrDefinition = item.getDefinition();
        Permission permission1 = new Permission(jcrDefinition.get__id(), Privilege.Read, true);
        jcrPermissionDao.grant(permission1, user);
    }

    @Override
    public void grant(Permission permission, String group) throws DatabaseException {
        JCRItem item = jcrItemDao.get(permission.getTargetId());
        jcrPermissionDao.grant(permission, group);
        JCRDefinition jcrDefinition = item.getDefinition();
        Permission permission1 = new Permission(jcrDefinition.get__id(), Privilege.Read , true);
        jcrPermissionDao.grant(permission1, group);
    }

}
