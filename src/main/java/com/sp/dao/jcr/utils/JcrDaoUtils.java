package com.sp.dao.jcr.utils;

import com.sp.dao.api.exchange.ExchangeProviderJcrToSp;
import com.sp.dao.jcr.model.JcrName;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.api.exchange.ConsumerProviderSpToJcr;
import com.sp.model.*;
import com.sp.service.StringSerialization;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class JcrDaoUtils {


    public static void setProperty(Node node, JcrName name, Value value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, Value[] values) throws RepositoryException {
        node.setProperty(name.name(), values);
    }

    public static void setProperty(Node node, JcrName name, String value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, String value, int type) throws RepositoryException {
        node.setProperty(name.name(), value, type);
    }

    public static void setProperty(Node node, JcrName name, String[] values) throws RepositoryException {
        node.setProperty(name.name(), values);
    }

    public static void setProperty(Node node, JcrName name, Binary value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, boolean value) throws RepositoryException {
        node.setProperty(name.name(), value);

    }

    public static void setProperty(Node node, JcrName name, double value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, long value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, Calendar value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static void setProperty(Node node, JcrName name, Node value) throws RepositoryException {
        node.setProperty(name.name(), value);
    }

    public static Node getNode(Node node, JcrName name) throws RepositoryException {
        if (!node.hasNode(name.name())) {
            return null;
        }
        return node.getNode(name.name());
    }

    public static Node createIfNotExist(Node node, JcrName name) throws RepositoryException {
        if (node.hasNode(name.name())) {
            return node.getNode(name.name());
        }
        return node.addNode(name.name());
    }

    public static Property getProperty(Node node, JcrName name) throws RepositoryException {
        return node.getProperty(name.name());
    }

    public static Node addNode(Node node, JcrName name) throws RepositoryException {
        return node.addNode(name.name());
    }


    /**
     * Return binary object from this node.
     *
     * @param node
     * @param binaryObject
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    public static <T extends BinaryData> T getBinaryFromNode(Node node, T binaryObject) throws RepositoryException {
        Node dataHolderNode = getNode(node, FixedNames.value()).getNode("jcr:content");
        Binary binary = dataHolderNode.getProperty("jcr:data").getBinary();
        binaryObject.mimeType(dataHolderNode.getProperty("jcr:mimeType").getString());
        binaryObject.inputStream(binary.getStream());
        return binaryObject;
    }

    /**
     * Return isArray of binary object from binary nodes those are connected with this node through value* name.
     *
     * @param node
     * @param binaryArrays
     * @param binaryFactory
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    public static <T extends BinaryData> T[] getBinariesFromNode(Node node, List<BinaryData> binaryArrays,
                                                                 BinaryFactory binaryFactory) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes(FixedNames.value().name() + "*");
        while (nodeIterator.hasNext()) {
            BinaryData holder = binaryFactory.getObject();
            Node dataHolderNode = nodeIterator.nextNode().getNode("jcr:content");
            Binary binary = dataHolderNode.getProperty("jcr:data").getBinary();
            holder.inputStream(binary.getStream());
            holder.mimeType(dataHolderNode.getProperty("jcr:mimeType").getString());
            binaryArrays.add(holder);
        }
        return (T[]) binaryArrays.toArray(new BinaryData[]{});
    }

    /**
     * Getting data from items fields and putting it back in node. Here also i am not able find any mechanism to get
     * a generic way to set dynamic typed field and hence the switch mess.
     *
     * @param fieldValueList Field list from itemObject.
     * @param fieldNode      field node representing this field list.
     * @throws Exception
     */
    public static void populateFieldsNodeFromItemFields(List<FieldValue> fieldValueList, Node fieldNode,

                                                        StringSerialization serialization,
                                                        ConsumerProviderSpToJcr<FieldValue> providerSpToJcr) throws
            Exception {

        for (FieldValue fieldValue : fieldValueList) {
            Node propertyNode = addNode(fieldNode, JcrNameFac.getUserName(fieldValue.getField().getName()));
            setProperty(propertyNode, FixedNames.field(), serialization.serialize(fieldValue.getField()));
            providerSpToJcr.apply(fieldValue.getField().getValueType()).accept(fieldValue, propertyNode);
        }
    }


    /**
     * Helper method to add binary content. Common for all video, image and file types.
     *
     * @param node
     * @param binaryData
     * @throws Exception
     */
    public static void addBinaryToNode(Node node, BinaryData binaryData, ValueFactory valueFactory)
            throws Exception {
        addBinaryToNode(node, binaryData, FixedNames.value(), valueFactory);
    }

    /**
     * @param node
     * @param binaryData
     * @param name
     * @throws Exception
     */
    private static void addBinaryToNode(Node node, BinaryData binaryData, JcrName name, ValueFactory valueFactory)
            throws Exception {
        Node fileFile = addNode(node, name);
        fileFile.setPrimaryType(NodeType.NT_FILE);
        Node fileContent = fileFile.addNode("jcr:content", NodeType.NT_RESOURCE);
        Binary fileBinary = valueFactory.createBinary(binaryData.inputStream());
        fileContent.setProperty("jcr:data", fileBinary);
        fileContent.setProperty("jcr:mimeType", binaryData.mimeType());
    }


    /**
     * Helper method to add isArray of binaries.
     *
     * @param node
     * @param binaryList
     * @throws Exception
     */
    public static void addBinariesToNode(Node node, BinaryData binaryList[], ValueFactory valueFactory) throws
            Exception {
        int i = 1;
        for (BinaryData binaryData : binaryList) {
            addBinaryToNode(node, binaryData, FixedNames.value(i), valueFactory);
            ++i;
        }
    }


    /**
     *
     * @param valueFactory
     * @param fieldValue
     * @param propertyNode
     * @throws RepositoryException
     */
    public static void setStringProp(ValueFactory valueFactory, FieldValue fieldValue, Node
            propertyNode) throws RepositoryException {
        if (fieldValue.getValue() instanceof String[]) {
            String[] array = (String[]) fieldValue.getValue();
            Value[] values = new Value[array.length];

            int count = 0;
            for (String each : array) {
                values[count++] = valueFactory.createValue(each);
            }
            setProperty(propertyNode, FixedNames.value(), values);
        } else {
            setProperty(propertyNode, FixedNames.value(), (String) fieldValue.getValue());
        }
    }


    public static Calendar createCalender(Date createDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createDate);
        return calendar;
    }


    /**
     * @param fieldNode
     * @return
     * @throws Exception
     */
    public static List<FieldValue> getFieldsFromNode(Node fieldNode, StringSerialization serialization,
                                                     ExchangeProviderJcrToSp<FieldValue> exchangeProviderJcrToSp) throws
            Exception {
        List<FieldValue> fieldValueList = new ArrayList<>();
        NodeIterator nodeIterator = fieldNode.getNodes(JcrNameFac.getUserName("*").pattern());
        while (nodeIterator.hasNext()) {
            Node propNode = nodeIterator.nextNode();
            fieldValueList.add(exchangeProviderJcrToSp.apply(propNode).apply(propNode));
        }
        return fieldValueList;
    }

    /**
     * This is different than fields in item. This field is only present on definition which is nested and this field
     * is json of field that represent the nested definition.
     *
     * @param node
     * @param serialization
     * @return
     * @throws IOException
     * @throws RepositoryException
     */
    public static Field getField(Node node, StringSerialization serialization) throws IOException, RepositoryException {
        String fieldStr = JcrDaoUtils.getProperty(node, FixedNames.field()).getString();
        return serialization.deserialize(fieldStr, Field.class);
    }
}
