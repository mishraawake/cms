package com.sp.dao.jcr.utils;

import com.sp.dao.api.DatabaseException;
import com.sp.model.*;
import com.sp.service.StringSerialization;
import org.apache.jackrabbit.commons.SimpleValueFactory;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class JcrDaoUtils {



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
    public static <T extends BinaryData> T[] getBinariesFromNode(Node node, List<BinaryData> binaryArrays , BinaryFactory objectgen ) throws RepositoryException {
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

    /**
     * Getting data from item fields and putting it back in node. Here also i am not able find any method get dynamic
     * method.
     *
     * @param fieldValueList
     * @param fieldNode
     * @param session
     * @throws Exception
     */
    public static void populateNodeFromItem(List<FieldValue> fieldValueList, Node fieldNode, Session session, StringSerialization serialization) throws Exception {

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
                        populateNodeFromItem(fieldValueListChild, propertyNode.addNode(JCRNodePropertyName.VALUE_LINK_NAME), session, serialization);
                    }
                    break;
                default:
                    throw new DatabaseException(new Exception("An unknown value type present in fieldlist"));
            }
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
    private static void addBinaryToNode(Node node, BinaryData binaryData, ValueFactory valueFactory, String name) throws Exception {

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
    private static void addBinarysToNode(Node node, BinaryData binaryList[], ValueFactory valueFactory) throws Exception {
        int i = 1;
        for (BinaryData binaryData : binaryList) {
            addBinaryToNode(node, binaryData, valueFactory, JCRNodePropertyName.VALUE_LINK_NAME + i);
            ++i;
        }
    }


    private static void setStringProp(SimpleValueFactory simpleValueFactory, FieldValue fieldValue, Node propertyNode) throws RepositoryException {
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


    public static Calendar createCalender(Date createDate) {
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
    public static List<FieldValue> getFieldsFromNode(Node itemNode, StringSerialization serialization) throws Exception {
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
                List<FieldValue> fieldValueListChild = getFieldsFromNode(valueNode, serialization);
                fieldValueList.add(FieldValue.getFieldValue(field, fieldValueListChild.toArray(new FieldValue[0])));
            }
        }
        return fieldValueList;
    }


    /**
     * Helper method to get double from node.
     *
     * @param fieldValueList
     * @param propNode
     * @param field
     * @throws RepositoryException
     */
    private static void getDoubleFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws RepositoryException {
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
    private static void getStringFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws RepositoryException {
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


}
