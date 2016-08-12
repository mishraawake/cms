package com.sp.dao.jcr.utils;

import com.sp.model.*;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class JcrDaoUtils {

    //  private final String JCR_TYPE = "sp:def";


    private static final String JCR_CHILD_INDEX_PROP = "child_index";


    /**
     * This logic is common to create a child node inside a node. This will copy two properties from parent node
     * and gene
     * @param parentNode
     * @return
     * @throws RepositoryException
     */
    public static Node getCreatingChild(Node parentNode, String pathPref) throws RepositoryException {

        int childIndex = 0;
        if (parentNode.hasProperty(JCR_CHILD_INDEX_PROP)) {
            childIndex = parentNode.getProperty(JCR_CHILD_INDEX_PROP).getDecimal().intValue();
        }

        parentNode.setProperty(JCR_CHILD_INDEX_PROP, childIndex + 1);
        Node childNode = parentNode.addNode(pathPref);
        childNode.addMixin(NodeType.MIX_REFERENCEABLE);
        return childNode;
    }

    public static String getPrefixedName(String name){
        return JCRRepository.MY_NAME_SPACE_PREFIX + ":" + name;
    }


    /**
     *
     * Return binary object from this node.
     * @param node
     * @param binaryObject
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    public static <T extends BinaryData> T getBinaryFromNode(Node node, T binaryObject) throws RepositoryException {
        Node dataHolderNode = node.getNode("value").getNode("jcr:content");
        Binary binary = dataHolderNode.getProperty("jcr:data").getBinary();
        binaryObject.mimeType( dataHolderNode.getProperty("jcr:mimeType").getString());
        binaryObject.inputStream(binary.getStream());
        return binaryObject;
    }

    /**
     * Return array of binary object from binary nodes those are connected with this node through value* name.
     * @param node
     * @param binaryArrays
     * @param objectgen
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    public static <T extends BinaryData> T[] getBinariesFromNode(Node node, List<BinaryData> binaryArrays , BinaryFacory objectgen ) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes("value*");
        while (nodeIterator.hasNext()){
            BinaryData holder = objectgen.getObject();
            Node dataHolderNode = nodeIterator.nextNode().getNode("jcr:content");
            Binary binary = dataHolderNode.getProperty("jcr:data").getBinary();
            holder.inputStream(binary.getStream());
            holder.mimeType( dataHolderNode.getProperty("jcr:mimeType").getString());
            binaryArrays.add(holder);
        }
        return (T[])binaryArrays.toArray(new BinaryData[]{});
    }
}
