package com.sp.utils.dump;

import com.sp.dao.jcr.utils.JCRRepository;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.io.Serializable;

/**
 * Created by pankajmishra on 13/08/16.
 *
 * A general purpose class.. Do all dirty stuff in this class.. correctness of this class ha not nor any bearing.
 */
public class JCRDump {

    public static void main(String[] args) throws RepositoryException {
        Session session = JCRRepository.getSession();
        Node tempNode =  session.getRootNode().addNode("temp");
        tempNode.addMixin(NodeType.MIX_REFERENCEABLE);

        Node referee = session.getRootNode().addNode("referee");
        referee.setProperty("tempone", tempNode);

        session.save();

        session = JCRRepository.getSession();

        referee = session.getNode("/referee");

        tempNode = session.getNode("/temp");

        System.out.println(referee.getProperties("tempone").hasNext());
        System.out.println(tempNode.getReferences().hasNext() );

    }
}
