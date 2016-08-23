package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.ItemDao;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.dao.api.exchange.ExchangeProviderJcrToSp;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.dao.jcr.model.JcrName;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.Association;
import com.sp.model.AssociationLink;
import com.sp.model.FieldValue;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 07/08/16.
 */
@Repository(value = "itemDao")
public class JCRItemDao implements ItemDao<JCRItem> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    JCRDefinitionDao jcrDefinitionDao;


    @Autowired
    JCRIRepository JCRIRepository;

    @Autowired
    ExchangeProviderJcrToSp<FieldValue> exchangeProviderJcrToSp;

    @Autowired
    ConsumerProviderSpToJcr<FieldValue> consumerProviderSpToJcr;

    @Override
    public JCRItem create(JCRItem element) throws DatabaseException {
        // for the parent items.
        if (element.getParentItem() != null) {
            // we must find parent items. So a caller of this method can not pass null object.
            return createChild(element, element.getParentItem());
        }
        return createChildFromParent(element, null);
    }

    @Override
    public List<JCRItem> getRootItems() throws DatabaseException {
        Session session = null;
        try {

            session = JCRIRepository.getSession();
            Node rootNode = session.getRootNode();
            NodeIterator nodeIterator = JcrDaoUtils.getNode(rootNode, FixedNames.items()).getNodes();
            List<JCRItem> items = new ArrayList<JCRItem>();
            while (nodeIterator.hasNext()) {
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

            session = JCRIRepository.getSession();
            Node node = session.getNode((String)element.get__id());
            JcrDaoUtils.setProperty(node, FixedNames.name(), element.getName() );
            JcrDaoUtils.populateFieldsNodeFromItemFields(element.getFieldValues(), JcrDaoUtils.getNode(node,
                            FixedNames.fields()), serialization, consumerProviderSpToJcr);
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
            session = JCRIRepository.getSession();
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
        item.setDefinition(
                jcrDefinitionDao.get( JcrDaoUtils.getProperty(itemNode, FixedNames.defs()).getString()));
        item.setName(
                JcrDaoUtils.getProperty(itemNode, FixedNames.name()).getString() );
        Node fieldNode = JcrDaoUtils.getNode(itemNode, FixedNames.fields());
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
            session = JCRIRepository.getSession();
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
            session = JCRIRepository.getSession();
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
            throw new DatabaseException(new Exception("prent items of to be created can not be null"));
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
            session = JCRIRepository.getSession();
            JCRItem item = get(id);
            Node itemNode = session.getNode((String)id);
            NodeIterator nodeIterator = itemNode.getNodes(JcrNameFac.getPackageName(name).pattern());

            while (nodeIterator.hasNext()) {
                Association association = new Association();
                Node associationNode = nodeIterator.nextNode();
                // get the field node of association node.
                Node associationFieldNode = JcrDaoUtils.getNode(associationNode, FixedNames.fields());
                List<FieldValue> assocFieldValueList = JcrDaoUtils.getFieldsFromNode(associationFieldNode,
                        serialization, exchangeProviderJcrToSp);
                association.setProperties(assocFieldValueList);
                association.setName( JcrDaoUtils.getProperty(associationNode, FixedNames.name()).getString());
                NodeIterator associatedLinkNodes = associationNode.getNodes(JcrNameFac.getPackageName("*").pattern());

                while (associatedLinkNodes.hasNext()) {
                    AssociationLink associationLink = new AssociationLink();
                    Node associatedLinkNode = associatedLinkNodes.nextNode();
                    Node assocLinkFieldNode = JcrDaoUtils.getNode(associatedLinkNode, FixedNames.fields());

                    List<FieldValue> assocLinkFieldValueList = JcrDaoUtils.getFieldsFromNode(assocLinkFieldNode,
                            serialization, exchangeProviderJcrToSp);
                    associationLink.setProperties(assocLinkFieldValueList);
                    PropertyIterator assocLinkPropIterator = associatedLinkNode.getProperties(JcrNameFac.getProjectName("*")
                            .pattern());
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
            List<JCRItem> items = new ArrayList<JCRItem>();
            session = JCRIRepository.getSession();
            Node itemNode = session.getNode((String) id);
            Node childNodeContainer = JcrDaoUtils.getNode(itemNode, FixedNames.child() );

            if (childNodeContainer != null) {
                if (childNodeContainer.hasNodes()) {
                    NodeIterator nodeIterator = childNodeContainer.getNodes();
                    while (nodeIterator.hasNext()) {
                        Node childNode = nodeIterator.nextNode();
                        JCRItem newItem = getJcrItemFromNode(childNode);
                        items.add(newItem);
                    }
                }
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
        if (item.getDefinition() == null) {
            throw new DatabaseException(new Exception(String.format("Definition of Item with name %s must be set.",
                    item.getName())));
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
    private Node getItemNode(Node parentNode, JcrName link) throws Exception {
        Node newlyCreated = JcrDaoUtils.addNode(parentNode, link);
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
            session = JCRIRepository.getSession();
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
     *
     * @param toBeCreated
     * @param parentObj
     * @param session
     * @return
     * @throws Exception
     */
    private JCRItem createChildInSession(JCRItem toBeCreated, JCRItem parentObj, Session session) throws
            Exception {

        // node that is going to created
        Node createdNodeItem = null;
        // definition path of definition items associated with this items.


        if (parentObj != null) {
            // This means that toBeCreated will be added as a child node to its parent.
            Node parentNode = session.getNode((String)parentObj.get__id());
            if (parentNode == null) {
                String expMessage = String.format("Child items not found information .. parent items id %s, " +
                                "child items id %s ", parentObj.get__id(), toBeCreated.get__id());
                throw new DatabaseException(new Exception(expMessage.toString()));
            }

            Node childContainerNode =  JcrDaoUtils.createIfNotExist(parentNode, FixedNames.child());
            createdNodeItem = getItemNode(childContainerNode, JcrNameFac.getUserName(toBeCreated.getIdentityForPath()) );

        } else {
            // creating in root.
            Node itemRootNode = JcrDaoUtils.createIfNotExist(session.getRootNode(), FixedNames.items());
            // This means that toBeCreated will be added as a link to root of jcr.
            createdNodeItem = getItemNode(itemRootNode, JcrNameFac.getUserName(toBeCreated.getIdentityForPath()));
        }
        populateNodeFromItem(toBeCreated, session, createdNodeItem);

        toBeCreated.set__id(createdNodeItem.getPath());

        // now we need to create associations...
        createAssociations(toBeCreated, createdNodeItem, session);

        // if we have children array present in item node.
        createChildrenOfNewlyCreated(toBeCreated, session);

        return toBeCreated;
    }

    /**
     * recursively create items.
     *
     * @param toBeCreated
     * @param session
     */
    private void createChildrenOfNewlyCreated(JCRItem toBeCreated, Session session) throws Exception {
        for (JCRItem childItem : toBeCreated.getChildren()) {
            // avoid the cycle.. very cheap cycle avoidance..
            if (childItem.get__id() == null) {
                createChildInSession(childItem, toBeCreated, session);
            }
        }
    }

    /**
     * @param toBeCreated
     * @param session
     * @param createdNodeItem
     * @throws Exception
     */
    private void populateNodeFromItem(JCRItem toBeCreated, Session session, Node createdNodeItem) throws Exception {
        String defPath = session.getNode((String) getDefinition(toBeCreated).get__id()).getPath();

        JcrDaoUtils.setProperty(createdNodeItem , FixedNames.name() , toBeCreated.getName() );
        JcrDaoUtils.setProperty(createdNodeItem , FixedNames.defs() , defPath, PropertyType.PATH);

        Node fieldNode = JcrDaoUtils.addNode(createdNodeItem, FixedNames.fields());

        JcrDaoUtils.populateFieldsNodeFromItemFields(toBeCreated.getFieldValues(), fieldNode, serialization,
                consumerProviderSpToJcr);
    }

    /**
     * Here createdItem is the node which has just created -- (possibly) -- and this method is trying to add
     * associations
     * of this items. createdNodeItem is corresponding node items. Algorithm is as follow.
     * 1. We check or create association node with its name and one field node.
     * 2. With association node we create a link node which in turn contains all the properties of AssociationLink
     * 3. Then with link node we create another link with actual items.
     * <p>
     * _____(field properties node)
     * ||
     * ||
     * // so the relationship will be (createdItem)--[ns:pkg][association-name]->(association node)===
     * //
     * //                                    _________(field properties node)
     * //                                   ||
     * //                                   ||
     * // [ns:pkg][linked items name]===>(link node)--[ns:ps][linked items name]-->(link items node)
     *
     * @param createdItem
     * @param createdNodeItem
     * @param session
     * @throws Exception
     */
    private void createAssociations(JCRItem createdItem, Node createdNodeItem, Session session) throws Exception {

        for (Association<JCRItem> association : createdItem.getAssociations()) {

            Node associationNode = JcrDaoUtils.getNode(createdNodeItem, JcrNameFac.getPackageName(association
                    .getIdentityForPath()));


            if (associationNode == null ){
                associationNode =  JcrDaoUtils.addNode(createdNodeItem, JcrNameFac.getPackageName(association
                        .getIdentityForPath()));
                JcrDaoUtils.setProperty(associationNode, FixedNames.name(), association.getName());
                JcrDaoUtils.setProperty(associationNode, FixedNames.createAt(), JcrDaoUtils.createCalender
                        (association.getCreateDate()));

                Node associationFieldNode = JcrDaoUtils.addNode(associationNode, FixedNames.fields());
                JcrDaoUtils.populateFieldsNodeFromItemFields(association.getProperties(), associationFieldNode,
                        serialization, consumerProviderSpToJcr);

                // TODO: for the time being not associating user. Please do it with perspective of jackrabbit.
                // user who has created this node.
            }

            for (AssociationLink<JCRItem> associationLink : association.getAssociates()) {
                JCRItem linkedItem = associationLink.getLinked();

                Node linkedNode =  JcrDaoUtils.getNode(associationNode, JcrNameFac.getPackageName(associationLink
                        .getLinked().getIdentityForPath()));

                if ( linkedNode != null){
                    // This is the case when this items is already associated as association  .. not
                    // have to do anything

                } else {
                    // we need to see whether this node is already created or not.
                    Node linkedItemNode, linkFieldNode;

                    linkedNode = JcrDaoUtils.addNode(associationNode, JcrNameFac.getPackageName(associationLink
                            .getLinked().getIdentityForPath()));


                    linkFieldNode = JcrDaoUtils.addNode(linkedNode, FixedNames.fields()) ;

                    JcrDaoUtils.populateFieldsNodeFromItemFields(associationLink.getProperties(), linkFieldNode,
                            serialization, consumerProviderSpToJcr);

                    if (linkedItem.get__id() != null) {
                        linkedItemNode = session.getNode((String)linkedItem.get__id());
                        // add the node as property.

                    } else {
                        // create this node.
                        // if linked items has already some parent associated with it.
                        if (linkedItem.getParentItem() != null) {
                            // do nothing
                        } else {
                            linkedItem.setParentItem(createdItem);
                        }
                        linkedItem = createChildInSession(linkedItem, linkedItem.getParentItem(), session);
                        linkedItemNode = session.getNode((String)linkedItem.get__id());
                    }
                    JcrDaoUtils.setProperty(linkedNode, JcrNameFac.getProjectName(associationLink.getLinked()
                            .getIdentityForPath()), linkedItemNode);
                }
            }
        }
    }


    private void populateItemFromNode(Node fieldNode, JCRItem item) throws Exception {
        item.setFieldValues(JcrDaoUtils.getFieldsFromNode(fieldNode, serialization, exchangeProviderJcrToSp));
    }


}



