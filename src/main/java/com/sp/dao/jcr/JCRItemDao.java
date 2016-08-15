package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.ItemDao;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.dao.jcr.utils.JCRNodePropertyName;
import com.sp.model.*;
import com.sp.service.StringSerialization;
import com.sp.dao.jcr.utils.JCRRepository;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.service.impl.JCRIdGenerator;
import org.apache.jackrabbit.commons.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.Serializable;
import java.util.*;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository(value = "itemDao")
public class JCRItemDao implements ItemDao<JCRItem> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    JCRDefinitionDao jcrDefinitionDao;

    @Override
    public JCRItem create(JCRItem element) throws DatabaseException {
        // for the parent item.
        if (element.getParentItem() != null) {
            // we must find parent item.
            return createChild(element, element.getParentItem());
        }
        return createChildFromParent(element, null);
    }

    @Override
    public JCRItem createOrUpdate(JCRItem element) throws DatabaseException {
        if (element.get__id() == null) {
            return create(element);
        }
        return update(element);
    }

    @Override
    public JCRItem update(JCRItem element) throws DatabaseException {
        Session session = null;
        try {

            session = JCRRepository.getSession();
            Node node = session.getNode(element.get__id());
            node.setProperty(JCRNodePropertyName.NAME_LINK_NAME, element.getName());
            JcrDaoUtils.populateNodeFromItem(element.getFieldValues(), node.getNode(JCRNodePropertyName.FIELDS_LINK_NAME), session, serialization);

            session.save();
            return element;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public JCRItem get(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            Node itemNode = session.getNode((String) id);
            JCRItem item = getJcrItemFromNode(itemNode);
            return item;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private JCRItem getJcrItemFromNode(Node itemNode) throws Exception {
        JCRItem item = new JCRItem();
        item.set__id(itemNode.getPath());
        item.setDefinition(jcrDefinitionDao.get(itemNode.getProperty(JCRNodePropertyName.DEF_LINK_NAME).getString()));
        item.setName(itemNode.getProperty(JCRNodePropertyName.NAME_LINK_NAME).getString());
        Node fieldNode = itemNode.getNode(JCRNodePropertyName.FIELDS_LINK_NAME);
        populateItemFromNode(fieldNode, item);
        return item;
    }

    @Override
    public Iterable<JCRItem> list() throws DatabaseException {
        return null;
    }

    @Override
    public Long count() throws DatabaseException {
        return null;
    }

    @Override
    public void delete(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            session.removeItem((String) id);
            session.save();
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public boolean exists(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            return session.nodeExists((String) id);
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public JCRItem createChild(JCRItem toBeCreated, JCRItem parentItem) throws DatabaseException {
        if (parentItem == null) {
            throw new DatabaseException(new Exception("to be created can not be null"));
        }
        return createChildFromParent(toBeCreated, parentItem);
    }

    @Override
    public JCRItem getItemWithAssociations(Serializable id) throws DatabaseException {
        return getItemWithAssociations(id, "*");
    }

    @Override
    public JCRItem getItemWithAssociations(Serializable id, String name) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            JCRItem item = get((String) id);
            Node itemNode = session.getNode((String) id);
            NodeIterator nodeIterator =  itemNode.getNodes(JCRRepository.PACKAGE_NAME_SPACE_PREFIX + ":"+name);

            while(nodeIterator.hasNext()){
                Association association = new Association();
                Node associationNode = nodeIterator.nextNode();
                // get the field node of assocition node.
                Node associationFieldNode =  associationNode.getNode(JCRNodePropertyName.FIELDS_LINK_NAME);
                List<FieldValue> assocFieldValueList = JcrDaoUtils.getFieldsFromNode(associationFieldNode, serialization);
                association.setProperties(assocFieldValueList);
                association.setName(associationNode.getProperty(JCRNodePropertyName.NAME_LINK_NAME).getString());
                NodeIterator associatedLinkNodes = associationNode.getNodes(JCRRepository.PACKAGE_NAME_SPACE_PREFIX + ":*");

                while (associatedLinkNodes.hasNext()){
                    AssociationLink associationLink = new AssociationLink();
                    Node associatedLinkNode = associatedLinkNodes.nextNode();
                    Node assocLinkFieldNode = associatedLinkNode.getNode(JCRNodePropertyName.FIELDS_LINK_NAME);
                    List<FieldValue> assocLinkFieldValueList = JcrDaoUtils.getFieldsFromNode(assocLinkFieldNode, serialization);
                    associationLink.setProperties(assocLinkFieldValueList);
                    PropertyIterator assocLinkPropIterator =  associatedLinkNode.getProperties(JCRRepository.MY_NAME_SPACE_PREFIX + ":*");
                    Node linkedItemNode = assocLinkPropIterator.nextProperty().getNode();
                    JCRItem linkedItem = getJcrItemFromNode(linkedItemNode);
                    associationLink.setLinked(linkedItem);
                    association.getAssociates().add(associationLink);
                }
                item.getAssociations().add(association);
            }


            return item;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public List<JCRItem> getChildItems(Serializable id) throws DatabaseException {
        Session session = null;
        try {

            session = JCRRepository.getSession();
            Node itemNode = session.getNode((String) id);
            NodeIterator nodeIterator =  itemNode.getNode(JCRNodePropertyName.CHILD_LINK_VALUE).getNodes();
            List<JCRItem> items = new ArrayList<JCRItem>();
            while(nodeIterator.hasNext()){
                Node childNode = nodeIterator.nextNode();
                JCRItem newItem = getJcrItemFromNode(childNode);
                items.add(newItem);
            }
            return items;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }


    @Override
    public JCRDefinition getDefinition(JCRItem item) throws DatabaseException {
        if(item.getDefinition() == null){
            throw new DatabaseException(new Exception(String.format("Definition of Item with name %s must be set.", item.getName() ) ));
        }
        return jcrDefinitionDao.get(item.getDefinition().get__id());
    }


    /////////////// Helper methods of this class begins hence...//////////////////////////////////////


    /**
     * Item node with referenceable.
     *
     * @param parentNode
     * @param link
     * @return
     * @throws Exception
     */
    private Node getItemNode(Node parentNode, String link) throws Exception {
        Node newlyCreated = parentNode.addNode(link);
        newlyCreated.addMixin(NodeType.MIX_REFERENCEABLE);
        return newlyCreated;
    }


    /**
     * This is main creation method. It takes alreadyCreated as parameter that is used as
     *
     * @param toBeCreated
     * @param alreadyCreated
     * @return
     * @throws DatabaseException
     */
    private JCRItem createChildFromParent(JCRItem toBeCreated, JCRItem alreadyCreated) throws DatabaseException {
        Session session = null;

        try {
            session = JCRRepository.getSession();
            toBeCreated = createChildInSession(toBeCreated, alreadyCreated, session);
            session.save();
            return toBeCreated;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    /**
     * Do not start or save the session.
     * @param toBeCreated
     * @param alreadyCreated
     * @param session
     * @return
     * @throws Exception
     */
    private JCRItem createChildInSession(JCRItem toBeCreated, JCRItem alreadyCreated, Session session) throws Exception {

        // node that is going to created
        Node createdNodeItem = null;
        // definition path of definition item associated with this item.


        if (alreadyCreated != null) {
            // This means that toBeCreated will be added as a link to another node which is already created.
            Node refNode = session.getNode(alreadyCreated.get__id());
            if (refNode == null) {
                String expMessage = String.format("Linked item not found information .. item id %s, " +
                                "liked item id %s , link name %s", toBeCreated.get__id(), alreadyCreated.get__id(),
                        JCRNodePropertyName.CHILD_LINK_VALUE);
                throw new DatabaseException(new Exception(expMessage.toString()));
            }

            Node itemRootNode = null;
            if(refNode.hasNode(JCRNodePropertyName.CHILD_LINK_VALUE)){
                itemRootNode = refNode.getNode(JCRNodePropertyName.CHILD_LINK_VALUE);
            } else {
                itemRootNode = refNode.addNode(JCRNodePropertyName.CHILD_LINK_VALUE);
            }


            createdNodeItem = getItemNode(itemRootNode, toBeCreated.getIdentityForPath());

        } else {
            // This means that toBeCreated will be added as a link to root of jcr.
            createdNodeItem = getItemNode(session.getRootNode(), toBeCreated.getIdentityForPath());
        }

        createNodeFromItem(toBeCreated, session, createdNodeItem);
        toBeCreated.set__id(createdNodeItem.getPath());
        // now we need to create associations...
        createAssociations(toBeCreated, createdNodeItem, session);

        return toBeCreated;
    }

    /**
     * @param toBeCreated
     * @param session
     * @param createdNodeItem
     * @throws Exception
     */
    private void createNodeFromItem(JCRItem toBeCreated, Session session, Node createdNodeItem) throws Exception {
        String defPath = session.getNode(getDefinition(toBeCreated).get__id()).getPath();
        createdNodeItem.setProperty(JCRNodePropertyName.NAME_LINK_NAME, toBeCreated.getName());
        createdNodeItem.setProperty(JCRNodePropertyName.DEF_LINK_NAME, defPath, PropertyType.PATH);
        Node fieldNode = createdNodeItem.addNode(JCRNodePropertyName.FIELDS_LINK_NAME);
        JcrDaoUtils.populateNodeFromItem(toBeCreated.getFieldValues(), fieldNode, session, serialization);
    }

    private String getPkgPrefixedName(String name) {
        return JCRRepository.PACKAGE_NAME_SPACE_PREFIX + ":" + name;
    }

    private String getGlobalPrefixedName(String name) {
        return JCRRepository.MY_NAME_SPACE_PREFIX + ":" + name;
    }


    /**
     * Here createdItem is the node which has just created -- (possibly) -- and this method is trying to add associations
     * of this item. createdNodeItem is corresponding node item. Algorithm is as follow.
     * 1. We check or create association node with its name and one field node.
     * 2. With association node we create a link node which in turn contains all the properties of AssociationLink
     * 3. Then with link node we create another link with actual item.
     * <p/>
     * _____(field properties node)
     * ||
     * ||
     * // so the relationship will be (createdItem)--[ns:pkg][association-name]->(association node)===
     * //
     * //                                    _________(field properties node)
     * //                                   ||
     * //                                   ||
     * // [ns:pkg][linked item name]===>(link node)--[ns:ps][linked item name]-->(link item node)
     *
     * @param createdItem
     * @param createdNodeItem
     * @param session
     * @throws Exception
     */
    private void createAssociations(JCRItem createdItem, Node createdNodeItem, Session session) throws Exception {
        for (Association<JCRItem> association : createdItem.getAssociations()) {
            Node associationNode = null;
            if (createdNodeItem.hasNode(getPkgPrefixedName(association.getIdentityForPath()))) {
                associationNode = createdNodeItem.getNode(association.getIdentityForPath());
            } else {
                associationNode = createdNodeItem.addNode(getPkgPrefixedName(association.getIdentityForPath()));
                associationNode.setProperty(JCRNodePropertyName.NAME_LINK_NAME, association.getName());
                associationNode.setProperty(JCRNodePropertyName.CREATED_AT_LINK_VALUE, JcrDaoUtils.createCalender(association.getCreateDate()));

                Node associationFieldNode = associationNode.addNode(JCRNodePropertyName.FIELDS_LINK_NAME);
                JcrDaoUtils.populateNodeFromItem(association.getProperties(), associationFieldNode, session, serialization);

                // TODO: for the time being not associating user. Please do it with perspective of jackrabbit.
                // user who has created this node.
            }

            for (AssociationLink<JCRItem> associationLink : association.getAssociates()) {
                JCRItem linkedItem = associationLink.getLinked();
                if (associationNode.hasNode(getPkgPrefixedName(associationLink.getLinked().getIdentityForPath()))) {
                    // This is the case when this item is already associated as association but as property .. not have to do anything

                } else {
                    // we need to see whether this node is already created or not.
                    Node linkedNode = null, linkedItemNode = null, linkFieldNode = null;

                    linkedNode = associationNode.addNode(getPkgPrefixedName(associationLink.getLinked().getIdentityForPath()));

                    linkFieldNode = linkedNode.addNode(JCRNodePropertyName.FIELDS_LINK_NAME);

                    JcrDaoUtils.populateNodeFromItem(associationLink.getProperties(), linkFieldNode, session, serialization);

                    if (linkedItem.get__id() != null) {
                        linkedItemNode = session.getNode(linkedItem.get__id());
                        // add the node as property.

                    } else {
                        // create this node.
                        // if linked item has already some parent associated with it.
                        if (linkedItem.getParentItem() != null) {
                            // do nothing
                        } else {
                            linkedItem.setParentItem(createdItem);
                        }
                        linkedItem = createChildInSession(linkedItem, linkedItem.getParentItem(), session);
                        linkedItemNode = session.getNode(linkedItem.get__id());
                    }

                    linkedNode.setProperty(getGlobalPrefixedName(associationLink.getLinked().getIdentityForPath()), linkedItemNode);
                }
            }
        }
    }



    private void populateItemFromNode(Node itemNode, JCRItem item) throws Exception {
        item.setFieldValues(JcrDaoUtils.getFieldsFromNode(itemNode, serialization));
    }


}



