package com.sp.utils.dump;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.commons.SimpleValueFactory;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import java.security.Principal;
import java.util.NoSuchElementException;

/**
 * Created by pankajmishra on 13/08/16.
 * <p>
 * A general purpose class.. Do all dirty stuff in this class.. correctness of this class has not not any bearing.
 */
public class JCRDump {

    public static void main(String[] args) throws RepositoryException {
        Repository repository = new Jcr(new Oak()).createRepository();
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        //  Session session = repository.login();

        //session.getRootNode().addNode("pankaj");
        JackrabbitSession jackrabbitSession = (JackrabbitSession) session;


        org.apache.jackrabbit.api.security.user.User user = jackrabbitSession.getUserManager().createUser("pankaj",
                "pankaj");

        user.setProperty("address", (new SimpleValueFactory()).createValue("HI this is my address"));

        AccessControlManager aMgr = session.getAccessControlManager();

        getAccessControlList(user, aMgr);

// the policy must be re-set
        // aMgr.setPolicy("/pankaj", acl);

        session.save();
        session.logout();

        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            Authorizable authorizable = ((JackrabbitSession) session).getUserManager().getAuthorizable("pankaj");
            System.out.println(authorizable.getProperty("address"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        session = repository.login(new SimpleCredentials("pankaj", "pankaj".toCharArray()));

        Node tempNode = session.getNode("/pankaj").addNode("temp");
        tempNode.addMixin(NodeType.MIX_REFERENCEABLE);

        session.save();

        Node referee = session.getRootNode().addNode("referee");
        referee.setProperty("tempone", tempNode);

        session.save();

        session = repository.login(new SimpleCredentials("pankaj", "pankaj".toCharArray()));

        referee = session.getNode("/referee");

        tempNode = session.getNode("/temp");

        System.out.println(referee.getProperties("tempone").hasNext());
        System.out.println(tempNode.getReferences().hasNext());

    }

    private static void createAType() throws RepositoryException {
        Repository repository = new Jcr(new Oak()).createRepository();
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));


    }

    private static AccessControlList getAccessControlList(User user, AccessControlManager aMgr) throws
            RepositoryException {
        // create a privilege set with jcr:all
        Privilege[] privileges = new Privilege[]{aMgr.privilegeFromName(Privilege.JCR_READ), aMgr.privilegeFromName
                (Privilege.JCR_ALL)};
        JackrabbitAccessControlList acl;
        try {
            // get first applicable policy (for nodes w/o a policy)
            acl = (JackrabbitAccessControlList) aMgr.getApplicablePolicies("/").nextAccessControlPolicy();
        } catch (NoSuchElementException e) {
            // else node already has a policy, get that one
            acl = (JackrabbitAccessControlList) aMgr.getPolicies("/")[0];
        }
// remove all existing entries
        for (AccessControlEntry e : acl.getAccessControlEntries()) {
            acl.removeAccessControlEntry(e);
        }
// add a new one for the special "everyone" principal

        for (String str : acl.getRestrictionNames()) {
            System.out.println(acl.getRestrictionType(str));
        }
        acl.addAccessControlEntry(user.getPrincipal(), privileges);
        return acl;
    }

    private static class UserDetail implements Principal {
        String name;

        String add;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "UserDetail{" +
                    "name='" + name + '\'' +
                    ", add='" + add + '\'' +
                    '}';
        }
    }
}
