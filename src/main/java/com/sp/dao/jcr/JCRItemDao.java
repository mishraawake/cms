package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.ItemDao;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.model.*;
import com.sp.service.StringSerialization;
import com.sp.service.impl.JCRIdGenerator;
import com.sp.dao.jcr.utils.JCRRepository;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import org.apache.jackrabbit.commons.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository(value = "itemDao")
public class JCRItemDao implements ItemDao<JCRItem> {


    @Autowired
    StringSerialization serialization;


    @Autowired
    JCRIdGenerator idGenerator;

    @Autowired
    JCRDefinitionDao jcrDefinitionDao;

    private void populateItemFromNode(Node itemNode, JCRItem item) throws Exception {

        item.set__id(itemNode.getPath());
        NodeIterator nodeIterator = itemNode.getNodes(JCRRepository.MY_NAME_SPACE_PREFIX + ":*");
        while (nodeIterator.hasNext()) {
            Node propNode = nodeIterator.nextNode();

            String fieldStr = propNode.getProperty("field").getString();

            Field field = serialization.deserialize(fieldStr, Field.class);

            if (propNode.hasNode("binarys") ) {

                Node binaryContainingNode = (Node) propNode.getNode("binarys");



                if (field.getValueType().equals(ValueType.Image)) {
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFacory.IMAGE_FACTORY.getObject()) ));
                } else if(field.getValueType().equals(ValueType.ArrayOfImage)){
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFacory.IMAGE_FACTORY  ) ) );
                } else if (field.getValueType().equals(ValueType.Video)) {
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFacory.VIDEO_FACTORY.getObject()) ));
                } else if (field.getValueType().equals(ValueType.ArrayOfVideo)) {
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFacory.VIDEO_FACTORY  ) ) );
                } else if (field.getValueType().equals(ValueType.File)) {
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinaryFromNode(binaryContainingNode, BinaryFacory.FILE_FACTORY.getObject()) ));
                } else if (field.getValueType().equals(ValueType.ArrayOfFile)) {
                    item.getFieldValues().add(FieldValue.getFieldValue(field, JcrDaoUtils.getBinariesFromNode(binaryContainingNode, new ArrayList<BinaryData>(), BinaryFacory.FILE_FACTORY  ) ) );
                }
            }

            if(propNode.hasProperty("value")) {


                // TODO: can we have little elegant way to do below task ?
                switch (field.getValueType().value()) {

                    case "boolean":
                        if(field.getValueType().array()){

                            Value[] values = propNode.getProperty("value").getValues();
                            boolean[] array = new boolean[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = eachValue.getBoolean();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getBoolean()));
                        }
                        break;
                    case "byte":
                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            byte[] array = new byte[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] =  (byte)eachValue.getLong() ;
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, Byte.valueOf(propNode.getProperty("value").getLong() + "" )));
                        }
                        break;
                    case "char":

                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            char[] array = new char[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = (char)eachValue.getLong();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, (char)propNode.getProperty("value").getLong()));
                        }
                        break;
                    case "short":
                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            short[] array = new short[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = (short)eachValue.getLong();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, (short)propNode.getProperty("value").getLong()));
                        }

                        break;
                    case "integer":
                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            int[] array = new int[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = (int)eachValue.getLong();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, (int)propNode.getProperty("value").getLong()));
                        }

                        break;
                    case "long":
                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            long[] array = new long[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = eachValue.getLong();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getLong()));
                        }
                        break;
                    case "double":
                        getDoubleFromNode(item, propNode, field);
                        break;
                    case "float":
                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            float[] array = new float[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = eachValue.getDecimal().floatValue();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getDecimal().floatValue()));
                        }
                        break;
                    case "date":

                        if(field.getValueType().array()){
                            Value[] values = propNode.getProperty("value").getValues();
                            Date[] array = new Date[values.length];
                            int count = 0;
                            for(Value eachValue : values){
                                array[count++] = eachValue.getDate().getTime();
                            }
                            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
                        } else {
                            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getDate().getTime()));
                        }

                        break;
                    case "string":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "bigstring":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "richtext":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "richmediatext":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "time":
                        getDoubleFromNode(item, propNode, field);
                        break;
                    case "enum":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "ref":
                        //TODO : will have to handle.
                        // propertyNode.setProperty("value", (Node) fieldValue.getValue());
                        break;
                    case "phone":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "email":
                        getStringFromNode(item, propNode, field);
                        break;
                    case "usergenerated":
                        getStringFromNode(item, propNode, field);
                        break;
                }
            }

        }
    }

    private void getDoubleFromNode(JCRItem item, Node propNode, Field field) throws RepositoryException {
        if(field.getValueType().array()){
            Value[] values = propNode.getProperty("value").getValues();
            double[] array = new double[values.length];
            int count = 0;
            for(Value eachValue : values){
                array[count++] = eachValue.getDouble();
            }
            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
        } else {
            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getDecimal()));
        }
    }

    private void getStringFromNode(JCRItem item, Node propNode, Field field) throws RepositoryException {
        if(field.getValueType().array()){
            Value[] values = propNode.getProperty("value").getValues();
            String[] array = new String[values.length];
            int count = 0;
            for(Value eachValue : values){
                array[count++] = eachValue.getString();
            }
            item.getFieldValues().add(FieldValue.getFieldValue(field, array));
        } else {
            item.getFieldValues().add(FieldValue.getFieldValue(field, propNode.getProperty("value").getString()));
        }
    }


    private void addBinaryToNode( Node node, BinaryData binaryData, ValueFactory  valueFactory, String name) throws Exception  {

        Node fileFile = node.addNode(name, "nt:file");
        Node fileContent = fileFile.addNode("jcr:content", "nt:resource");
        Binary fileBinary = valueFactory.createBinary(binaryData.inputStream());
        fileContent.setProperty("jcr:data", fileBinary);
        fileContent.setProperty("jcr:mimeType", binaryData.mimeType());
    }


    private void addBinarysToNode( Node node, BinaryData binaryList[], ValueFactory  valueFactory) throws Exception  {
        int i = 1;
        for(BinaryData binaryData : binaryList){
            addBinaryToNode(node, binaryData, valueFactory, "value"+i);
            ++i;
        }
    }

    private void populateNodeFromItem(JCRItem toBeCreated, Node fieldNode, Session session) throws Exception {

        SimpleValueFactory simpleValueFactory = new SimpleValueFactory();
        for (FieldValue fieldValue : toBeCreated.getFieldValues()) {
            Node propertyNode = fieldNode.addNode(JcrDaoUtils.getPrefixedName(fieldValue.getField().getName()));
            propertyNode.setProperty("field", serialization.serialize(fieldValue.getField()));

            // TODO: can we have little elegant way to do below task ?
            switch (fieldValue.getField().getValueType().value()) {

                case "boolean":
                    if(fieldValue.getValue() instanceof Boolean[]){
                        Boolean[] array = (Boolean[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Boolean eachBool : array) {
                            values[count++] = simpleValueFactory.createValue(eachBool);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Boolean) fieldValue.getValue());
                    }
                    break;
                case "byte":
                    if(fieldValue.getValue() instanceof Byte[]){
                        Byte[] array = (Byte[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Byte each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Byte) fieldValue.getValue());
                    }
                    break;
                case "char":

                    if(fieldValue.getValue() instanceof Character[]){
                        Character[] array = (Character[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Character each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Character) fieldValue.getValue());
                    }

                    break;
                case "short":
                    if(fieldValue.getValue() instanceof Short[]){
                        Short[] array = (Short[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Short each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Short) fieldValue.getValue());
                    }

                    break;
                case "integer":
                    if(fieldValue.getValue() instanceof Integer[]){
                        Integer[] array = (Integer[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Integer each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Integer) fieldValue.getValue());
                    }

                    break;
                case "long":
                    if(fieldValue.getValue() instanceof Long[]){
                        Long[] array = (Long[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Long each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Long) fieldValue.getValue());
                    }
                    break;
                case "double":
                    if(fieldValue.getValue() instanceof Double[]){
                        Double[] array = (Double[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Double each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Double) fieldValue.getValue());
                    }
                    break;
                case "float":
                    if(fieldValue.getValue() instanceof Float[]){
                        Float[] array = (Float[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Float each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Float) fieldValue.getValue());
                    }
                    break;
                case "date":


                    if(fieldValue.getValue() instanceof Date[]){
                        Date[] array = (Date[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Date each : array) {
                            Calendar  calendar = new GregorianCalendar();
                            calendar.setTime(each);
                            values[count++] = simpleValueFactory.createValue(calendar);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        Calendar  calendar = new GregorianCalendar();
                        calendar.setTime((Date)fieldValue.getValue()  );
                        propertyNode.setProperty("value", calendar);
                    }

                    break;
                case "string":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "bigstring":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "richtext":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "richmediatext":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "image":

                    Node fileImages = propertyNode.addNode("binarys");
                    if(fieldValue.getValue() instanceof Image[]){
                        addBinarysToNode(fileImages, (Image[])fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileImages, (Image)fieldValue.getValue(), session.getValueFactory(), "value");
                    }

                    break;
                case "video":

                    Node fileVideos = propertyNode.addNode("binarys");
                    if(fieldValue.getValue() instanceof Video[]){
                        addBinarysToNode(fileVideos, (Video[])fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileVideos, (Video)fieldValue.getValue(), session.getValueFactory(), "value");
                    }
                    break;
                case "file":
                    Node fileObjects = propertyNode.addNode("binarys");
                    if(fieldValue.getValue() instanceof FileObject[]){
                        addBinarysToNode(fileObjects, (FileObject[])fieldValue.getValue(), session.getValueFactory());
                    } else {
                        addBinaryToNode(fileObjects, (FileObject)fieldValue.getValue(), session.getValueFactory(), "value");
                    }
                    break;
                case "time":
                    if(fieldValue.getValue() instanceof Double[]){
                        Double[] array = (Double[])fieldValue.getValue();
                        Value[] values =  new Value[array.length];

                        int count = 0;
                        for(Double each : array) {
                            values[count++] = simpleValueFactory.createValue(each);
                        }
                        propertyNode.setProperty("value", values);
                    } else {
                        propertyNode.setProperty("value", (Double) fieldValue.getValue());
                    }
                    break;
                case "enum":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "ref":
                    //TODO : will have to handle.
                   // propertyNode.setProperty("value", (Node) fieldValue.getValue());
                    break;
                case "phone":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "email":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
                case "valueType":
                    setStrignProp(simpleValueFactory, fieldValue, propertyNode);
                    break;
            }
        }
    }

    private void setStrignProp(SimpleValueFactory simpleValueFactory, FieldValue fieldValue, Node propertyNode) throws RepositoryException {
        if(fieldValue.getValue() instanceof String[]){
            String[] array = (String[])fieldValue.getValue();
            Value[] values =  new Value[array.length];

            int count = 0;
            for(String each : array) {
                values[count++] = simpleValueFactory.createValue(each);
            }
            propertyNode.setProperty("value", values);
        } else {
            propertyNode.setProperty("value", (String) fieldValue.getValue());
        }
    }

    @Override
    public JCRItem create(JCRItem element) throws DatabaseException {
        return createWithLink(element, null, idGenerator.getNextId(element));
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
            populateNodeFromItem(element, node.getNode("fields"), session);
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
            JCRItem item = new JCRItem();
            session = JCRRepository.getSession();
            Node itemNode = session.getNode((String) id);
            Node fieldNode = itemNode.getNode("fields");
            populateItemFromNode(fieldNode, item);
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
    public JCRItem createWithLink(JCRItem toBeCreated, JCRItem alreadyCreated, String linkName) throws DatabaseException {
        Session session = null;

        try {
            session = JCRRepository.getSession();
            Node curItem = null;
            if (alreadyCreated != null) {
                Node refNode = session.getNode(alreadyCreated.get__id());
                curItem = JcrDaoUtils.getCreatingChild(refNode, linkName);
            } else {
                curItem = JcrDaoUtils.getCreatingChild(session.getRootNode(), linkName);
            }

            String path = "";
            if(alreadyCreated != null) {
                path = session.getNode(getDefinition(toBeCreated).get__id()).getPath();
            } else {
                path = session.getNode(getDefinition(toBeCreated).get__id()).getPath();
            }
            curItem.setProperty("def", path, PropertyType.PATH);
            Node fieldNode = curItem.addNode("fields");
            populateNodeFromItem(toBeCreated, fieldNode, session);
            toBeCreated.set__id(curItem.getPath());
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

    @Override
    public JCRDefinition getDefinition(JCRItem item) throws DatabaseException {
       return jcrDefinitionDao.get( item.getDefinition().get__id() );
    }

}



