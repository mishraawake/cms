package com.sp.dao.jcr.utils;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.jcr.model.JcrName;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.model.*;
import com.sp.service.StringSerialization;

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
        if(!node.hasNode(name.name())){
            return null;
        }
        return node.getNode(name.name());
    }

    public static Node createIfNotExist(Node node, JcrName name) throws RepositoryException {
        if(node.hasNode(name.name())){
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
        Node dataHolderNode = getNode(node, FixedNames.value() ).getNode("jcr:content");
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
     * @param objectgen
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    public static <T extends BinaryData> T[] getBinariesFromNode(Node node, List<BinaryData> binaryArrays,
                                                                 BinaryFactory objectgen) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes( FixedNames.value().name() + "*");
        while (nodeIterator.hasNext()) {
            BinaryData holder = objectgen.getObject();
            Node dataHolderNode = nodeIterator.nextNode().getNode("jcr:content");
            Binary binary = dataHolderNode.getProperty("jcr:data").getBinary();
            holder.inputStream(binary.getStream());
            holder.mimeType(dataHolderNode.getProperty("jcr:mimeType").getString());
            binaryArrays.add(holder);
        }
        return (T[]) binaryArrays.toArray(new BinaryData[]{});
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
    public static void populateNodeFromItem(List<FieldValue> fieldValueList, Node fieldNode, Session session,
                                            StringSerialization serialization) throws Exception {


        for (FieldValue fieldValue : fieldValueList) {
            Node propertyNode =  addNode(fieldNode, JcrNameFac.getUserName(fieldValue.getField()
                    .getName()));

            setProperty(propertyNode, FixedNames.field(), serialization.serialize(fieldValue.getField()));

            // TODO: can we have little elegant way to do below task ?
            switch (fieldValue.getField().getValueType()) {

                case ArrayOfBoolean: {
                    boolean[] array = (boolean[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (boolean eachBool : array) {
                        values[count++] = session.getValueFactory().createValue(eachBool);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                }
                break;
                case Boolean:
                    setProperty(propertyNode, FixedNames.value(), (boolean) fieldValue.getValue());
                    break;
                case ArrayOfByte: {
                    byte[] array = (byte[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (byte each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                }
                break;

                case Byte:
                    setProperty(propertyNode, FixedNames.value(), (byte) fieldValue.getValue());
                    break;
                case ArrayOfChar: {

                    char[] array = (char[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (char each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);

                    break;
                }
                case Char:
                    setProperty(propertyNode, FixedNames.value(), (char) fieldValue.getValue());
                    break;
                case ArrayOfShort: {
                    short[] array = (short[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (short each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }
                case Short:
                    setProperty(propertyNode, FixedNames.value(), (short) fieldValue.getValue());
                    break;
                case ArrayOfInteger: {
                    int[] array = (int[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (int each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }

                case Integer:
                    setProperty(propertyNode, FixedNames.value(), (int) fieldValue.getValue());
                    break;
                case ArrayOfLong: {
                    long[] array = (long[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (long each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);

                    break;
                }
                case Long:
                    setProperty(propertyNode, FixedNames.value(), (long) fieldValue.getValue());
                    break;
                case ArrayOfDouble: {
                    double[] array = (double[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Double each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }
                case Double:
                    setProperty(propertyNode, FixedNames.value(), (double) fieldValue.getValue());
                    break;
                case ArrayOfFloat: {
                    float[] array = (float[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Float each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                     setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }
                case Float:
                    setProperty(propertyNode, FixedNames.value(), (float) fieldValue.getValue());
                    break;
                case ArrayOfDate: {
                    Date[] array = (Date[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Date each : array) {
                        values[count++] = session.getValueFactory().createValue(createCalender(each));
                    }
                     setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }
                case Date:
                    setProperty(propertyNode, FixedNames.value(), createCalender((Date) fieldValue
                            .getValue()));

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
                    setStringProp(session.getValueFactory(), fieldValue, propertyNode);
                    break;
                case ArrayOfImage: {
                    Node fileImages = addNode(propertyNode, FixedNames.binaries());
                    addBinarysToNode(fileImages, (Image[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case Image: {
                    Node fileImages = addNode(propertyNode, FixedNames.binaries());
                    addBinaryToNode(fileImages, (Image) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case ArrayOfVideo: {
                    Node fileVideos = addNode(propertyNode, FixedNames.binaries());
                    addBinarysToNode(fileVideos, (Video[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case Video: {
                    Node fileVideos = addNode(propertyNode, FixedNames.binaries());
                    addBinaryToNode(fileVideos, (Video) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case ArrayOfFile: {
                    Node fileObjects = addNode(propertyNode, FixedNames.binaries());
                    addBinarysToNode(fileObjects, (FileObject[]) fieldValue.getValue(), session.getValueFactory());
                    break;
                }

                case File: {
                    Node fileObjects = addNode(propertyNode, FixedNames.binaries());
                    addBinaryToNode(fileObjects, (FileObject) fieldValue.getValue(), session.getValueFactory());
                    break;
                }
                case ArrayOfTime: {
                    double[] array = (double[]) fieldValue.getValue();
                    Value[] values = new Value[array.length];

                    int count = 0;
                    for (Double each : array) {
                        values[count++] = session.getValueFactory().createValue(each);
                    }
                    setProperty(propertyNode, FixedNames.value(), values);
                    break;
                }
                case Time:
                    setProperty(propertyNode, FixedNames.value(), (double) fieldValue.getValue());
                    break;
                case Ref:
                case ArrayOfRef:
                    //TODO : will have to handle.
                    // propertyNode.setProperty(JCRNodePropertyName.VALUE_LINK_NAME, (Node) fieldValue.getValue());
                    break;

                case Definition:
                    if (fieldValue.getValue() instanceof FieldValue[]) {
                        FieldValue[] fieldValues = (FieldValue[]) fieldValue.getValue();
                        List<FieldValue> fieldValueListChild = new ArrayList<FieldValue>();
                        for (FieldValue eachFieldValue : fieldValues) {
                            fieldValueListChild.add(eachFieldValue);
                        }
                        populateNodeFromItem(fieldValueListChild, addNode(propertyNode, FixedNames.value()),
                                session, serialization);
                    }
                    break;
                default:
                    throw new DatabaseException(new Exception("An unknown value type present in fieldList " +
                            fieldValue.getField().getValueType()));
            }
        }
    }


    /**
     * Helper method to add binary content. Common for all video, image and file types.
     *
     * @param node
     * @param binaryData
     * @throws Exception
     */
    private static void addBinaryToNode(Node node, BinaryData binaryData,  ValueFactory valueFactory)
            throws Exception {
        addBinaryToNode(node, binaryData, FixedNames.value(), valueFactory);
    }

    /**
     *
     * @param node
     * @param binaryData
     * @param name
     * @throws Exception
     */
    private static void addBinaryToNode(Node node, BinaryData binaryData, JcrName name, ValueFactory valueFactory )
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
    private static void addBinarysToNode(Node node, BinaryData binaryList[],  ValueFactory valueFactory) throws
            Exception {
        int i = 1;
        for (BinaryData binaryData : binaryList) {
            addBinaryToNode(node, binaryData,  FixedNames.value(i), valueFactory);
            ++i;
        }
    }


    private static void setStringProp(ValueFactory valueFactory, FieldValue fieldValue, Node
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
     * We need to improve this function. Right now , i am not getting any suitable mechanism to decipher type
     * of dynamic object value.
     *
     * @param itemNode
     * @return
     * @throws Exception
     */
    public static List<FieldValue> getFieldsFromNode(Node itemNode, StringSerialization serialization) throws
            Exception {
        List<FieldValue> fieldValueList = new ArrayList<FieldValue>();

        NodeIterator nodeIterator = itemNode.getNodes(JcrNameFac.getUserName("*").pattern());

        while (nodeIterator.hasNext()) {
            Node propNode = nodeIterator.nextNode();

            String fieldStr =  getProperty(propNode, FixedNames.field()).getString();

            Field field = serialization.deserialize(fieldStr, Field.class);

            // TODO: can we have little elegant way to do below task ?
            switch (field.getValueType()) {


                case ArrayOfBoolean: {

                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    boolean[] array = new boolean[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = eachValue.getBoolean();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }

                case Boolean:
                    fieldValueList.add(FieldValue.getFieldValue(field, getProperty(propNode, FixedNames.value())
                            .getBoolean()));
                    break;
                case ArrayOfByte: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    byte[] array = new byte[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = (byte) eachValue.getLong();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }

                case Byte:
                    fieldValueList.add(FieldValue.getFieldValue(field, (byte)getProperty(propNode,
                            FixedNames.value()).getLong() ));
                    break;
                case ArrayOfChar: {

                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    char[] array = new char[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = (char) eachValue.getLong();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));

                    break;
                }
                case Char:
                    fieldValueList.add(FieldValue.getFieldValue(field,
                            (char) getProperty(propNode, FixedNames.value()).getLong()));
                    break;
                case ArrayOfShort: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    short[] array = new short[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = (short) eachValue.getLong();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }
                case Short:
                    fieldValueList.add(FieldValue.getFieldValue(field,
                            (short) getProperty(propNode, FixedNames.value()).getLong()));

                    break;
                case ArrayOfInteger: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    int[] array = new int[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = (int) eachValue.getLong();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }

                case Integer:
                    fieldValueList.add(FieldValue.getFieldValue(field,
                            (int) getProperty(propNode, FixedNames.value()).getLong()));
                    break;
                case ArrayOfLong: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    long[] array = new long[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = eachValue.getLong();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }
                case Long:

                    fieldValueList.add(FieldValue.getFieldValue(field,
                            getProperty(propNode, FixedNames.value())
                            .getLong()));

                    break;
                case ArrayOfDouble:
                case Double:
                    getDoubleFromNode(fieldValueList, propNode, field);
                    break;
                case ArrayOfFloat: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    float[] array = new float[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = eachValue.getDecimal().floatValue();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }
                case Float:
                    fieldValueList.add(FieldValue.getFieldValue(field, getProperty(propNode, FixedNames.value())
                            .getDecimal().floatValue()));
                    break;
                case ArrayOfDate: {
                    Value[] values = getProperty(propNode, FixedNames.value()).getValues();
                    Date[] array = new Date[values.length];
                    int count = 0;
                    for (Value eachValue : values) {
                        array[count++] = eachValue.getDate().getTime();
                    }
                    fieldValueList.add(FieldValue.getFieldValue(field, array));
                    break;
                }
                case Date:
                    fieldValueList.add(FieldValue.getFieldValue(field, getProperty(propNode, FixedNames.value())
                            .getDate().getTime()));
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
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode
                            (binaryContainingNode, BinaryFactory.IMAGE_FACTORY.getObject())));
                    break;
                }
                case ArrayOfImage: {
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode
                            (binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.IMAGE_FACTORY)));
                    break;
                }
                case Video: {
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode
                            (binaryContainingNode, BinaryFactory.VIDEO_FACTORY.getObject())));
                    break;
                }
                case ArrayOfVideo: {
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode
                            (binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.VIDEO_FACTORY)));
                    break;
                }
                case File: {
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode
                            (binaryContainingNode, BinaryFactory.FILE_FACTORY.getObject())));
                    break;
                }
                case ArrayOfFile: {
                    Node binaryContainingNode = getNode(propNode, FixedNames.binaries());
                    fieldValueList.add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode
                            (binaryContainingNode, new ArrayList<BinaryData>(), BinaryFactory.FILE_FACTORY)));
                    break;
                }
                case Definition: {
                    Node valueNode = getNode(propNode, FixedNames.value());
                    if (valueNode != null) {
                        // it must be nested property.
                        List<FieldValue> fieldValueListChild = getFieldsFromNode(valueNode, serialization);
                        fieldValueList.add(FieldValue.getFieldValue(field, fieldValueListChild.toArray(new
                                FieldValue[0])));
                    } else if (field.getValueType().equals(ValueType.Definition)) {
                        fieldValueList.add(FieldValue.getFieldValue(field, new FieldValue[0]));
                    }
                    break;
                }
                default:
                    throw new DatabaseException(new Exception("An unknown value type present in fieldList " + field
                            .getValueType()));
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
    private static void getDoubleFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws
            RepositoryException {
        if (field.getValueType().isArray()) {
            Value[] values = getProperty(propNode, FixedNames.value()).getValues();
            double[] array = new double[values.length];
            int count = 0;
            for (Value eachValue : values) {
                array[count++] = eachValue.getDouble();
            }
            fieldValueList.add(FieldValue.getFieldValue(field, array));
        } else {
            fieldValueList.add(FieldValue.getFieldValue(field, getProperty(propNode, FixedNames.value()).getDecimal()
                    .doubleValue()));
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
    private static void getStringFromNode(List<FieldValue> fieldValueList, Node propNode, Field field) throws
            RepositoryException {
        if (field.getValueType().isArray()) {
            Value[] values = getProperty(propNode, FixedNames.value()).getValues();
            String[] array = new String[values.length];
            int count = 0;
            for (Value eachValue : values) {
                array[count++] = eachValue.getString();
            }
            fieldValueList.add(FieldValue.getFieldValue(field, array));
        } else {
            fieldValueList.add(FieldValue.getFieldValue(field, getProperty(propNode, FixedNames.value()).getString()));
        }
    }
}
