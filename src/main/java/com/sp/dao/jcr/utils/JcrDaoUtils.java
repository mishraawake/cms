package com.sp.dao.jcr.utils;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.JCRIRepository;
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
        return JCRIRepository.MY_NAME_SPACE_PREFIX + ":" + name;
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
            switch (fieldValue.getField().getValueType()) {

                case ArrayOfBoolean:
                {
                    boolean[] array = (boolean[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (boolean eachBool : array) {
                        values[count++] = simpleValueFactory.createValue(eachBool);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                }
                    break;
                case Boolean:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (boolean) fieldValue.getValue());
                    break;
                case ArrayOfByte:
                {
                    byte[] array = (byte[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (byte each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                }
                    break;

                case Byte:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (byte) fieldValue.getValue());
                    break;
                case ArrayOfChar: {

                    char[] array = (char[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (char each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);

                    break;
                }
                case Char:

                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (char) fieldValue.getValue());
                    break;
                case ArrayOfShort: {
                    short[] array = (short[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (short each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    break;
                }
                case Short:


                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (short) fieldValue.getValue());

                    break;
                case ArrayOfInteger: {
                    int[] array = (int[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (int each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    break;
                }

                case Integer:

                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (int) fieldValue.getValue());


                    break;
                case ArrayOfLong: {
                    long[] array = (long[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (long each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);

                    break;
                }
                case Long:

                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (long) fieldValue.getValue());

                    break;
                case ArrayOfDouble: {
                    double[] array = (double[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Double each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    break;
                }
                case Double:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (double) fieldValue.getValue());
                    break;
                case ArrayOfFloat: {
                        float[] array = (float[]) fieldValue.getValue();
                        Value[] values = new Value[array.length];

                        int count = 0;
                        for (Float each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                        break;
                    }
                case Float:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (float) fieldValue.getValue());
                    break;
                case ArrayOfDate:
                {
                    Date[] array = (Date[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Date each : array) {
                        values[count++] = simpleValueFactory.createValue(createCalender(each));
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    break;
                }
                case Date:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, createCalender((Date) fieldValue.getValue()));
                    break;

                case String:
                case ArrayOfString:
                case BigString:
                case ArrayOfBigString:
                case RichText:
                case ArrayOfRichText:
                case RichMediaText:
                case ArrayOfRichMediaText:
                case ArrayOfEnum:
                case Enum:
                case ArrayOfPhone:
                case Phone:
                case ArrayOfEmail:
                case Email:
                    setStringProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case ArrayOfImage: {
                    Node fileImages = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinarysToNode(fileImages, (Image[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case Image: {
                    Node fileImages = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinaryToNode(fileImages, (Image) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    break;
                }
                case ArrayOfVideo:{
                    Node fileVideos = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinarysToNode(fileVideos, (Video[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case Video: {
                    Node fileVideos = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinaryToNode(fileVideos, (Video) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    break;
                }
                case ArrayOfFile:{
                    Node fileObjects = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinarysToNode(fileObjects, (FileObject[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }

                case File:{
                    Node fileObjects = propertyNode.addNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                    addBinaryToNode(fileObjects, (FileObject) fieldValue.getValue(), session.getValueFactory(), JCRNodePropertyName.VALUE_LINK_NAME);
                    break;
                }
                case ArrayOfTime:{
                    double[] array = (double[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Double each : array) {
                        values[count++] = simpleValueFactory.createValue(each);
                    }
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, values);
                    break;
                }
                case Time:
                    propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (double) fieldValue.getValue());
                    break;
                case Ref:
                case ArrayOfRef:
                    //TODO : will have to handle.
                    // propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (Node) fieldValue.getValue());
                    break;

                case GENERIC_TYPE:
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
                    throw new DatabaseException(new Exception("An unknown value type present in fieldList "+fieldValue.getField().getValueType()));
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

        NodeIterator nodeIterator = itemNode.getNodes(JCRIRepository.MY_NAME_SPACE_PREFIX + ":*");

        while (nodeIterator.hasNext()) {
            Node propNode = nodeIterator.nextNode();

            String fieldStr = propNode.getProperty(JCRNodePropertyName.FIELD_LINK_NAME).getString();

            Field field = serialization.deserialize(fieldStr, Field.class);

                // TODO: can we have little elegant way to do below task ?
                switch (field.getValueType()) {


                    case ArrayOfBoolean: {

                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        boolean[] array = new boolean[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = eachValue.getBoolean();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }

                    case Boolean:
                        fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getBoolean()));
                        break;
                    case ArrayOfByte: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        byte[] array = new byte[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = (byte) eachValue.getLong();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }

                    case Byte:
                        fieldValueList.add(FieldValue.getFieldValue(field, Byte.valueOf(propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong() + "")));
                        break;
                    case ArrayOfChar: {

                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        char[] array = new char[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = (char) eachValue.getLong();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));

                        break;
                    }
                    case Char:
                        fieldValueList.add(FieldValue.getFieldValue(field, (char) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        break;
                    case ArrayOfShort: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        short[] array = new short[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = (short) eachValue.getLong();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }
                    case Short:
                        fieldValueList.add(FieldValue.getFieldValue(field, (short) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));

                        break;
                    case ArrayOfInteger: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        int[] array = new int[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = (int) eachValue.getLong();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }

                    case Integer:
                        fieldValueList.add(FieldValue.getFieldValue(field, (int) propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));
                        break;
                    case ArrayOfLong: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        long[] array = new long[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = eachValue.getLong();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }
                    case Long:

                        fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getLong()));

                        break;
                    case ArrayOfDouble:
                    case Double:
                        getDoubleFromNode(fieldValueList, propNode, field);
                        break;
                    case ArrayOfFloat: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        float[] array = new float[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = eachValue.getDecimal().floatValue();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }
                    case Float:
                        fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getDecimal().floatValue()));
                        break;
                    case ArrayOfDate: {
                        Value[] values = propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getValues();
                        Date[] array = new Date[values.length];
                        int count = 0;
                        for (Value eachValue : values) {
                            array[count++] = eachValue.getDate().getTime();
                        }
                        fieldValueList.add(FieldValue.getFieldValue(field, array));
                        break;
                    }
                    case Date:
                        fieldValueList.add(FieldValue.getFieldValue(field, propNode.getProperty(JCRNodePropertyName.VALUE_LINK_NAME).getDate().getTime()));
                        break;

                    case String:
                    case ArrayOfString:
                    case BigString:
                    case ArrayOfBigString:
                    case RichText:
                    case ArrayOfRichText:
                    case RichMediaText:
                    case ArrayOfRichMediaText:
                    case ArrayOfEnum:
                    case Enum:
                    case ArrayOfPhone:
                    case Phone:
                    case ArrayOfEmail:
                    case Email:
                        getStringFromNode(fieldValueList, propNode, field);
                        break;

                    case ArrayOfTime:
                    case Time:
                        getDoubleFromNode(fieldValueList, propNode, field);
                        break;
                    case Ref:
                    case ArrayOfRef:
                        //TODO : will have to handle.
                        // propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (Node) fieldValue.getValue());
                        break;
                    case Image: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.IMAGE_FACTORY.getObject())));
                        break;
                    }
                    case ArrayOfImage: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.IMAGE_FACTORY)));
                        break;
                    }
                    case Video: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.VIDEO_FACTORY.getObject())));
                        break;
                    }
                    case ArrayOfVideo: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.VIDEO_FACTORY)));
                        break;
                    }
                    case File: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFactory.FILE_FACTORY.getObject())));
                        break;
                    }
                    case ArrayOfFile: {
                        Node binaryContainingNode = (Node) propNode.getNode(JCRNodePropertyName.BINARIES_LINK_NAME);
                        fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.FILE_FACTORY)));
                        break;
                    }
                    case GENERIC_TYPE:
                        if (propNode.hasNode(JCRNodePropertyName.VALUE_LINK_NAME) ) {
                            // it must be nested property.
                            Node valueNode = propNode.getNode(JCRNodePropertyName.VALUE_LINK_NAME);
                            List<FieldValue> fieldValueListChild = getFieldsFromNode(valueNode, serialization);
                            fieldValueList.add(FieldValue.getFieldValue(field, fieldValueListChild.toArray(new FieldValue[0])));
                        } else if(field.getValueType().equals(ValueType.GENERIC_TYPE)) {
                            fieldValueList.add(FieldValue.getFieldValue(field, new FieldValue[0]));
                        }
                        break;
                    default:
                        throw new DatabaseException(new Exception("An unknown value type present in fieldList " + field.getValueType()));
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
