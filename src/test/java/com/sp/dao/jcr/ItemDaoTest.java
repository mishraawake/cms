package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.api.ItemDao;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.model.*;
import com.sp.utils.DefUtils;
import com.sp.utils.ItemUtils;
import com.sp.utils.SpringInitializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by pankajmishra on 08/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class ItemDaoTest implements ApplicationContextAware {

    ApplicationContext applicationContext;

    ItemDao<IItem> itemDao;

    DefinitionDao<IDefinition> definitionDao;

    @Autowired
    PojoFactory pojoFactory;

    @Test
    public void testCreationAndRetrieval() throws DatabaseException {
        itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);
        IDefinition definition = DefUtils.getDummyDefition(pojoFactory);
        definition = definitionDao.create(definition);
        IItem<IItem, IDefinition> item = ItemUtils.getDummyItem(pojoFactory);
        item.setDefinition(definition);
        item = itemDao.create(item);
        Serializable id = item.get__id();
        IItem<IItem, IDefinition> dataBaseItem = pojoFactory.getNewItem();
        dataBaseItem = itemDao.get(id);
        verifyItems(item, dataBaseItem);
    }

    private void verifyItems(IItem<IItem, IDefinition> sourceItem, IItem<IItem, IDefinition> targetItem){
        Assert.assertTrue(String.format("item name should be same %s, %s", sourceItem, targetItem), sourceItem.getName().equals(targetItem.getName()));
        Assert.assertTrue(String.format("item definition should be same %s, %s", sourceItem.getDefinition(), targetItem.getDefinition()), sourceItem.getDefinition().get__id().equals(targetItem.getDefinition().get__id()));
        verifyFieldLists(sourceItem.getFieldValues().toArray(new FieldValue[0]), targetItem.getFieldValues().toArray(new FieldValue[0]), 1);
    }

    @Test
    public void testCreationWithAssociation() throws DatabaseException {

        itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);


        IDefinition definition = DefUtils.getDummyDefition(pojoFactory);
        definition = definitionDao.create(definition);
        IItem<IItem, IDefinition> item = ItemUtils.getDummyItem(pojoFactory);
        item.setDefinition(definition);

        generateBunchOfAssociations(definition, item);

        // save this item.. associated item should be saved.
        item = itemDao.create(item);

        Serializable id = item.get__id();
        IItem<IItem, IDefinition> dataBaseItem = pojoFactory.getNewItem();
        dataBaseItem = itemDao.getItemWithAssociations(id);
        verifyItems(item, dataBaseItem);

        for(int associationCount = 0; associationCount < item.getAssociations().size(); ++associationCount){
            Association<IItem> association = item.getAssociations().get(associationCount);
            Association<IItem> databaseAssociation = item.getAssociations().get(associationCount);
            verifyAssociation(association, databaseAssociation);
        }
    }


    @Test
    public void testItemChildren() throws DatabaseException {

        itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);

        IDefinition definition = DefUtils.getDummyDefition(pojoFactory);
        definition = definitionDao.create(definition);
        IItem<IItem, IDefinition> parentItem = ItemUtils.getDummyItem(pojoFactory);
        parentItem.setDefinition(definition);
        parentItem = itemDao.create(parentItem);

        List<IItem> childItems = new ArrayList<>();
        for(int childCount = 0; childCount < 10; ++childCount){
            IItem<IItem, IDefinition> childItem = ItemUtils.getDummyItem(pojoFactory);
            childItem.setDefinition(definition);
            childItem.setParentItem(parentItem);
            childItem = itemDao.createChild(childItem, parentItem);
            childItems.add(childItem);
        }

        List<IItem> dbChildItems = itemDao.getChildItems(parentItem.get__id());

        for(int childCount=0; childCount < childItems.size(); ++childCount){
            IItem<IItem, IDefinition> savedChild = childItems.get(childCount);
            IItem<IItem, IDefinition> dbChildItem = dbChildItems.get(childCount);
            verifyItems(savedChild, dbChildItem);
        }
    }



    private void generateBunchOfAssociations(IDefinition definition, IItem<IItem, IDefinition> item) {
        for(int associationCount = 0; associationCount < 10; ++associationCount){
            Association<IItem> association = new Association<IItem>();
            association.setName("priority"+associationCount);
            association.setProperties(ItemUtils.getDummyFieldValueList(1));
            association.setCreateDate(new Date());
            item.getAssociations().add(association);

            for(int link=0; link < 10; ++link){
                IItem<IItem, IDefinition> associatedItem = ItemUtils.getDummyItem(pojoFactory);
                AssociationLink<IItem> associationLink = new AssociationLink<IItem>();
                associatedItem.setDefinition(definition);
                associationLink.setLinked(associatedItem);
                associationLink.setProperties(ItemUtils.getDummyFieldValueList(1));
                association.getAssociates().add(associationLink);
            }
        }
    }


    private void verifyAssociation(Association<IItem> association, Association<IItem> databaseAssociation) {


        Assert.assertTrue(String.format("association name should match %s, %s",association,  databaseAssociation), association.getName().equals(databaseAssociation.getName()));
        Assert.assertTrue(String.format("association date should match %s, %s", association,  databaseAssociation), association.getCreateDate().equals(databaseAssociation.getCreateDate()));

        verifyFieldLists(association.getProperties().toArray(new FieldValue[0]), databaseAssociation.getProperties().toArray(new FieldValue[0]), 1);

        for(int link=0; link < association.getAssociates().size(); ++link){
            AssociationLink<IItem> associationLink = association.getAssociates().get(link);
            AssociationLink<IItem> databaseAssociationLink = databaseAssociation.getAssociates().get(link);
            verifyFieldLists(associationLink.getProperties().toArray(new FieldValue[0]), databaseAssociationLink.getProperties().toArray(new FieldValue[0]), 1);
            verifyItems(associationLink.getLinked(), databaseAssociationLink.getLinked() );
        }
    }


    private void verifyFieldLists(FieldValue[] one, FieldValue[] second, int level) {

        for (FieldValue fieldValue : one) {

            //System.out.println(fieldValue.getField().getName());

            if (fieldValue.getValue() instanceof int[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())), fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((int[]) fieldValue.getValue(), (int[]) targetFieldValue.getValue()));
            } else if (fieldValue.getValue() instanceof boolean[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((boolean[]) fieldValue.getValue(), (boolean[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof byte[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((byte[]) fieldValue.getValue(), (byte[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof char[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((char[]) fieldValue.getValue(), (char[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof short[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((short[]) fieldValue.getValue(), (short[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof int[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((int[]) fieldValue.getValue(), (int[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof long[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((long[]) fieldValue.getValue(), (long[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof float[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((float[]) fieldValue.getValue(), (float[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof double[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),(fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((double[]) fieldValue.getValue(), (double[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof FieldValue[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                verifyFieldLists((FieldValue[]) fieldValue.getValue(), (FieldValue[]) targetFieldValue.getValue(), level + 1);
                // System.out.println(fieldValue.equals(getFieldValue(second, fieldValue.getField())));
            }else if(fieldValue.getValue() instanceof BinaryData || fieldValue.getValue() instanceof BinaryData[]){
                /// do nothing
            } else if(fieldValue.getValue() instanceof Object[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())), (Arrays.equals((Object[]) fieldValue.getValue(), (Object[]) targetFieldValue.getValue())));
                // fieldValue.equals(getFieldValue(second, fieldValue.getField())));
            } else {
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())), (fieldValue.equals(getFieldValue(second, fieldValue.getField()))));
            }


            //generateBibaries(fieldValue);
        }
    }

    private String getFieldDetail(FieldValue first, FieldValue second){
        return String.format("do not match, first value is ( %s ) and second value is ( %s )", first, second);
    }

    private void generateBibaries(FieldValue fieldValue) {
        if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfImage) || fieldValue.getField().getValueType().equals(ValueType.Image)) {
            //System.out.println();
            try {

                if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfImage)) {
                    FileOutputStream fileOutputStream = new FileOutputStream("output/image.jpg");
                    fileOutputStream.write(((Image) fieldValue.getValue()).getBytes());
                    fileOutputStream.close();
                }

            } catch (Exception e) {
            }
        } else if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfVideo) || fieldValue.getField().getValueType().equals(ValueType.Video)) {
            //System.out.println();
            try {
                if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfVideo)) {
                    FileOutputStream fileOutputStream = new FileOutputStream("output/video.mp4");
                    fileOutputStream.write(((Video) fieldValue.getValue()).getBytes());
                    fileOutputStream.close();
                }
            } catch (Exception e) {
            }
        } else if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfFile) || fieldValue.getField().getValueType().equals(ValueType.File)) {
            //System.out.println();
            try {
                if (fieldValue.getField().getValueType().equals(ValueType.ArrayOfFile)) {
                    FileOutputStream fileOutputStream = new FileOutputStream("output/file.pdf");
                    fileOutputStream.write(((FileObject) fieldValue.getValue()).getBytes());
                    fileOutputStream.close();
                }
            } catch (Exception e) {
            }
        } else {

        }
    }

    private FieldValue getFieldValue(FieldValue[] fromList, Field field) {
        for (FieldValue fieldValue : fromList) {
            if (fieldValue.getField().equals(field)) {
                return fieldValue;
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
