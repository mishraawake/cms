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
    JCRIdGenerator jcrIdGenerator;

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
            populateNodeFromItem(element.getFieldValues(), node.getNode(JCRNodePropertyName.FIELDS_LINK_NAME), session);

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
                List<FieldValue> assocFieldValueList = getFieldsFromNode(associationFieldNode);
                association.setProperties(assocFieldValueList);
                association.setName(associationNode.getProperty(JCRNodePropertyName.NAME_LINK_NAME).getString());
                NodeIterator associatedLinkNodes = associationNode.getNodes(JCRRepository.PACKAGE_NAME_SPACE_PREFIX + ":*");

                while (associatedLinkNodes.hasNext()){
                    AssociationLink associationLink = new AssociationLink();
                    Node associatedLinkNode = associatedLinkNodes.nextNode();
                    Node assocLinkFieldNode = associatedLinkNode.getNode(JCRNodePropertyName.FIELDS_LINK_NAME);
                    List<FieldValue> assocLinkFieldValueList = getFieldsFromNode(assocLinkFieldNode);
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

            Node linkedNode = null;
            if(refNode.hasNode(JCRNodePropertyName.CHILD_LINK_VALUE)){
                linkedNode = refNode.getNode(JCRNodePropertyName.CHILD_LINK_VALUE);
            } else {
                linkedNode = refNode.addNode(JCRNodePropertyName.CHILD_LINK_VALUE);
            }


            createdNodeItem = getItemNode(linkedNode, toBeCreated.getIdentityForPath());

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
        populateNodeFromItem(toBeCreated.getFieldValues(), fieldNode, session);
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
                associationNode.setProperty(JCRNodePropertyName.CREATED_AT_LINK_VALUE, createCalender(association.getCreateDate()));

                Node associationFieldNode = associationNode.addNode(JCRNodePropertyName.FIELDS_LINK_NAME);
                populateNodeFromItem(association.getProperties(), associationFieldNode, session);

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

                    populateNodeFromItem(associationLink.getProperties(), linkFieldNode, session);

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

    private Calendar createCalender(Date createDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createDate);
        return calendar;
    }



    /**
     * We need to improve this function. Right now , i am not getting any suitable mechanism to decipher type
     * of dynamic object value.
     *
     * @param itemNode
     * @return
     * @throws Exception
     */
    private List<FieldValue> getFieldsFromNode(Node itemNode) throws Exception {
        List<FieldValue> fieldValueList = new ArrayList<FieldValue>();

        NodeIterator nodeIterator = itemNode.getNodes(JCRRepository.MY_NAME_SPACE_PREFIX + ":*");

        while (nodeIterator.hasNext()) {
            Node propNode = nodeIterator.nextNode();

            String fieldStr = propNode.getProperty(JCRNodePropertyName.FIELD_LINK_NAME).getString();

            Field field = serialization.deserialize(fieldStr, Field.class);

            if (propNode.hasNode(JCRNodePropertyName.BINARIES_LINK_NAME)) {

                Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);

                if (field.getValueType().equals(ValueType.Image)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.IMAGE_FACTORY.getObject())));
                } else if (field.getValueType().equals(ValueType.ArrayOfImage)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.IMAGE_FACTORY)));
                } else if (field.getValueType().equals(ValueType.Video)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.VIDEO_FACTORY.getObject())));
                } else if (field.getValueType().equals(ValueType.ArrayOfVideo)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.VIDEO_FACTORY)));
                } else if (field.getValueType().equals(ValueType.File)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.FILE_FACTORY.getObject())));
                } else if (field.getValueType().equals(ValueType.ArrayOfFile)) {
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.FILE_FACTORY)));
                }
            }

            if (propNode.hasProperty(JCRNodePropertyName.VALUE_LINK_NAME)) {


                // TODO: can we have little elegant way to do below task ?
                switch (field.getValueType().value()) {

                    case ValueType.TYPE_BOOLEAN:
                        if (field.getValueType().array()) {

                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            boolean[] array = new boolean[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = eachValue.getBoolean();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getBoolean()));
                        }
                        break;
                    case ValueType.TYPE_BYTE:
                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            byte[] array = new byte[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = (byte) eachValue.getLong();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, Byte.valueOf(propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong() + "")));
                        }
                        break;
                    case ValueType.TYPE_CHAR:

                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            char[] array = new char[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = (char) eachValue.getLong();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, (char) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        }
                        break;
                    case ValueType.TYPE_SHORT:
                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            short[] array = new short[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = (short) eachValue.getLong();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, (short) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        }

                        break;
                    case ValueType.TYPE_INTEGER:
                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            int[] array = new int[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = (int) eachValue.getLong();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, (int) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        }

                        break;
                    case ValueType.TYPE_LONG:
                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            long[] array = new long[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = eachValue.getLong();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        }
                        break;
                    case ValueType.TYPE_DOUBLE:
                        getDoubleFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_FLOAT:
                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            float[] array = new float[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = eachValue.getDecimal().floatValue();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getDecimal().floatValue()));
                        }
                        break;
                    case ValueType.TYPE_DATE:

                        if (field.getValueType().array()) {
                            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                            Date[] array = new Date[values.length];
                            int count = 0;
                            for (Value eachValue : values) {
                                array[count++] = eachValue.getDate().getTime();
                            }
                            fieldValueList.add(FieldValue.getFieldValue(field, array));
                        } else {
                            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getDate().getTime()));
                        }

                        break;
                    case ValueType.TYPE_STRING:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_BIG_STRING:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_RICH_TEXT:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_RICH_MEDIA_TEXT:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_TIME:
                        getDoubleFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_ENUM:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_REF:
                        //TODO : will have to handle.
                        // propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (Node) fieldValue.getValue());
                        break;
                    case ValueType.TYPE_PHONE:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    case ValueType.TYPE_EMAIL:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;
                    default:
                       throw new DatabaseException(new Exception("An unknown value type present in node"));
                }
            }

            if (propNode.hasNode(JCRNodePropertyName.VALUE_LINK_NAME)) {
                // it must be nested property.
                Node valueNode = propNode.getNode(JCRNodePropertyName.VALUE_LINK_NAME);
                List<FieldValue> fieldValueListChild = getFieldsFromNode(valueNode);
                fieldValueList.add(FieldValue.getFieldValue(field, fieldValueListChild.toArray(new FieldValue[0])));
            }
        }
        return fieldValueList;
    }

    private void populateItemFromNode(Node itemNode, JCRItem item) throws Exception {
        item.setFieldValues(getFieldsFromNode(itemNode));
    }

    /**
     * Helper method to get double from node.
     *
     * @param fieldValueList
     * @param propNode
     * @param field
     * @throws RepositoryException
     */
    private void getDoubleFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws RepositoryException {
        if (field.getValueType().array()) {
            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
            double[] array = new double[values.length];
            int count = 0;
            for (Value eachValue : values) {
                array[count++] = eachValue.getDouble();
            }
            fieldValueList.add(FieldValue.getFieldValue(field, array));
        } else {
            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getDecimal().doubleValue()));
        }
    }

    /**
     * Helper method to get string.
     *
     * @param fieldValueList
     * @param propNode
     * @param field
     * @throws RepositoryException
     */
    private void getStringFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws RepositoryException {
        if (field.getValueType().array()) {
            Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
            String[] array = new String[values.length];
            int count = 0;
            for (Value eachValue : values) {
                array[count++] = eachValue.getString();
            }
            fieldValueList.add(FieldValue.getFieldValue(field, array));
        } else {
            fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getString()));
        }
    }


    /**
     * Helper method to add binary content. Common for all video, image and file types.
     *
     * @param node
     * @param binaryData
     * @param valueFactory
     * @param name
     * @throws Exception
     */
    private void addBinaryToNode(Node node, BinaryData binaryData, ValueFactory valueFactory, String name) throws Exception {

        Node fileFile = node.addNode(name, NodeType.NT_FILE);
        Node fileContent = fileFile.addNode("jcr:content", NodeType.NT_RESOURCE);
        Binary fileBinary = valueFactory.createBinary(binaryData.inputStream());
        fileContent.setProperty("jcr:data", fileBinary);
        fileContent.setProperty("jcr:mimeType", binaryData.mimeType());
    }


    /**
     * Helper method to add array of binaries.
     *
     * @param node
     * @param binaryList
     * @param valueFactory
     * @throws Exception
     */
    private void addBinarysToNode(Node node, BinaryData binaryList[], ValueFactory valueFactory) throws Exception {
        int i = 1;
        for (BinaryData binaryData : binaryList) {
            addBinaryToNode(node, binaryData, valueFactory, JCRNodePropertyName.VALUE_LINK_NAME + i);
            ++i;
        }
    }

    /**
     * Getting data from item fields and putting it back in node. Here also i am not able find any method get dynamic
     * method.
     *
     * @param fieldValueList
     * @param fieldNode
     * @param session
     * @throws Exception
     */
    private void populateNodeFromItem(List<FieldValue> fieldValueList, Node fieldNode, Session session) throws Exception {

        SimpleValueFactory simpleValueFactory = new SimpleValueFactory();



        for (FieldValue fieldValue : fieldValueList) {
            Node propertyNode = fieldNode.addNode(JcrDaoUtils.getPrefixedName(fieldValue.getField().getName()));
            propertyNode.setProperty(JCRNodePropertyName.FIELD_LINK_NAME, serialization.serialize(fieldValue.getField()));

            // TODO: can we have little elegant way to do below task ?
            switch (fieldValue.getField().getValueType().value()) {

                case ValueType.TYPE_BOOLEAN:
                    if (fieldValue.getValue() instanceof boolean[]) {
                        boolean[] array = (boolean[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (boolean eachBool : array) {
                            values[count++] = simpleValueFactory.createValue(eachBool);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (boolean) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_BYTE:
                    if (fieldValue.getValue() instanceof byte[]) {
                        byte[] array = (byte[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (byte each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (byte) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_CHAR:

                    if (fieldValue.getValue() instanceof char[]) {
                        char[] array = (char[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (char each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (char) fieldValue.getValue());
                    }

                    break;
                case ValueType.TYPE_SHORT:
                    if (fieldValue.getValue() instanceof short[]) {
                        short[] array = (short[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (short each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (short) fieldValue.getValue());
                    }

                    break;
                case ValueType.TYPE_INTEGER:
                    if (fieldValue.getValue() instanceof int[]) {
                        int[] array = (int[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (int each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (int) fieldValue.getValue());
                    }

                    break;
                case ValueType.TYPE_LONG:
                    if (fieldValue.getValue() instanceof long[]) {
                        long[] array = (long[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (long each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (long) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_DOUBLE:
                    if (fieldValue.getValue() instanceof double[]) {
                        double[] array = (double[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (Double each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (double) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_FLOAT:
                    if (fieldValue.getValue() instanceof float[]) {
                        float[] array = (float[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (Float each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (float) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_DATE:


                    if (fieldValue.getValue() instanceof Date[]) {
                        Date[] array = (Date[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (Date each : array) {
                            values[count++] = simpleValueFactory.createValue(createCalender(each));
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, createCalender((Date) fieldValue.getValue()));
                    }

                    break;
                case ValueType.TYPE_STRING:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_BIG_STRING:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_RICH_TEXT:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_RICH_MEDIA_TEXT:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_IMAGE:

                    Node fileImages = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    if (fieldValue.getValue() instanceof Image[]) {
                        addBinarysToNode(fileImages, (Image[]) fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileImages, (Image) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    }

                    break;
                case ValueType.TYPE_VIDEO:

                    Node fileVideos = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    if (fieldValue.getValue() instanceof Video[]) {
                        addBinarysToNode(fileVideos, (Video[]) fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileVideos, (Video) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    }
                    break;
                case ValueType.TYPE_FILE:
                    Node fileObjects = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    if (fieldValue.getValue() instanceof FileObject[]) {
                        addBinarysToNode(fileObjects, (FileObject[]) fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileObjects, (FileObject) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    }
                    break;
                case ValueType.TYPE_TIME:
                    if (fieldValue.getValue() instanceof double[]) {
                        double[] array = (double[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (Double each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    } else {
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (double) fieldValue.getValue());
                    }
                    break;
                case ValueType.TYPE_ENUM:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_REF:
                    //TODO : will have to handle.
                    // propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (Node) fieldValue.getValue());
                    break;
                case ValueType.TYPE_PHONE:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_EMAIL:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ValueType.TYPE_GENERIC_TYPE:
                    if (fieldValue.getValue() instanceof FieldValue[]) {
                        FieldValue[] fieldValues = (FieldValue[]) fieldValue.getValue();
                        List<FieldValue> fieldValueListChild = new ArrayList<FieldValue>();
                        for (FieldValue eachFieldValue : fieldValues) {
                            fieldValueListChild.add(eachFieldValue);
                        }
                        populateNodeFromItem(fieldValueListChild, propertyNode.addNode(JCRNodePropertyName.VALUE_LINK_NAME), session);
                    }
                    break;
                default:
                    throw new DatabaseException(new Exception("An unknown value type present in fieldlist"));
            }
        }
    }

    private void setStringProp(SimpleValueFactory simpleValueFactory, FieldValue fieldValue, Node propertyNode) throws RepositoryException {
        if (fieldValue.getValue() instanceof String[]) {
            String[] array = (String[]) fieldValue.getValue();
            Value[] values = new Value[array.length];

            int count = 0;
            for (String each : array) {
                values[count++] = simpleValueFactory.createValue(each);
            }
            propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
        } else {
            propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (String) fieldValue.getValue());
        }
    }

}



