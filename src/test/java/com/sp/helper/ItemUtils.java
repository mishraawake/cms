package com.sp.helper;

import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.dao.jcr.model.JCRUser;
import com.sp.model.*;
import com.sp.validate.constraint.GreaterThanConstraint;
import com.sp.validate.constraint.LengthConstraint;
import com.sp.validate.constraint.LessThanConstraint;
import com.sp.validate.constraint.RangeConstraint;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

/**
 * Created by pankajmishra on 08/08/16.
 */
public class ItemUtils {

    public static IItem getDummyItem(PojoFactory pojoFactory) {

        IItem item = pojoFactory.getNewItem();
        item.setName(RandomStringUtils.randomAlphabetic(50));
        item.setFieldValues(getDummyFieldValueList(1));
        item.setCreateDate(new Date());
        return item;
    }

    public static IItem<IItem, IDefinition> getDummyItemForDefinition(PojoFactory pojoFactory, IDefinition
            iDefinition) {
        IItem item = pojoFactory.getNewItem();
        Random random = new Random();
        item.setName(iDefinition.getName() + " - " + random.nextLong());
        item.setCreateDate(new Date());
        for (Field definitionField : iDefinition.getFields()) {
            FieldValue fieldValue = new FieldValue();
            fieldValue.setField(definitionField);
            fieldValue.setValue(DummyValueType.getDummyType(definitionField.getValueType(), 1));
            item.getFieldValues().add(fieldValue);
        }
        item.setDefinition(iDefinition);
        return item;
    }


    public static IItem getDummyItemWitnRandomField(PojoFactory pojoFactory) {
        IItem item = pojoFactory.getNewItem();
        item.setName(RandomStringUtils.randomAlphabetic(50));
        item.setFieldValues(getRandomFieldValueList());
        item.setCreateDate(new Date());
        return item;
    }


    private static void populateRandomNumberOfChildBetween10_50(IItem<IItem, IDefinition> parent, PojoFactory
            pojoFactory, IDefinition definition) {
        int min = 10, child;
        Random random = new Random();
        child = random.nextInt(40) + min;
        for (int i = 0; i < child; ++i) {
            IItem<IItem, IDefinition> item = getDummyItemWitnRandomField(pojoFactory);
            item.setDefinition(definition);
            parent.getChildren().add(item);
        }
    }


    public static IItem getItemTree(PojoFactory pojoFactory, IDefinition definition, int depth) {

        if (depth > 5) {
            throw new IllegalArgumentException("Depth is too much to handle = " + depth);
        }

        final String sentinel = "sentinel";
        IItem<IItem, IDefinition> sentinelItem = pojoFactory.getNewItem();
        sentinelItem.setName(sentinel);

        Random random = new Random(50);
        // create root Item
        IItem rootItem = pojoFactory.getNewItem();
        rootItem.setName("ROOTItem");
        rootItem.setFieldValues(getRandomFieldValueList());
        rootItem.setCreateDate(new Date());
        rootItem.setDefinition(definition);
        Queue<IItem<IItem, IDefinition>> queue = new LinkedList<>();
        queue.add(rootItem);
        queue.add(sentinelItem);

        int numberOfDepth = 0, eachChild = 0;
        while (!queue.isEmpty()) {
            IItem<IItem, IDefinition> queueItem = queue.poll();
            // we have a new level. Please push one more sentinel.
            if (queueItem.getName().equals(sentinel) && queueItem.getCreateDate() == null) {
                numberOfDepth++;
                queue.add(sentinelItem);
                eachChild = 0;
            }
            if (numberOfDepth < depth) {
                populateRandomNumberOfChildBetween10_50(queueItem, pojoFactory, definition);
                for (IItem<IItem, IDefinition> childOfChild : queueItem.getChildren()) {
                    childOfChild.setName((eachChild++) + " th Child Of Level " + numberOfDepth);
                    queue.add(childOfChild);
                }
            } else {
                break;
            }
        }

        return rootItem;
    }


    public static List<FieldValue> getDummyFieldValueList(int depth) {
        if (depth <= 0) {
            return new ArrayList<FieldValue>();
        }
        List<FieldValue> fieldValueList = new ArrayList<FieldValue>();
        for (int i = 0; i < ValueType.values().length; ++i) {
            Field field = new Field();
            field.setName(ValueType.values()[i].toString() + "" + i);
            GreaterThanConstraint greaterThanConstraint = new GreaterThanConstraint(5, true);
            field.getConstraints().add(greaterThanConstraint);
            LessThanConstraint lessThanConstraint = new LessThanConstraint(new Date(), true);
            field.getConstraints().add(lessThanConstraint);
            RangeConstraint rangeThanConstraint = new RangeConstraint(2l, true, 50.4, false);
            field.getConstraints().add(rangeThanConstraint);
            LengthConstraint lengthThanConstraint = new LengthConstraint(100);
            field.getConstraints().add(lengthThanConstraint);
            field.setSearchable(true);
            field.setValueType((ValueType.values()[i]));
            FieldValue fieldValue = new FieldValue<>();
            fieldValue.setField(field);
            Object dummyObj = DummyValueType.getDummyType(ValueType.values()[i], depth);
            if (dummyObj != null) {
                fieldValue.setValue(dummyObj);
                fieldValueList.add(fieldValue);
            }
        }
        return fieldValueList;
    }


    public static List<FieldValue> getRandomFieldValueList() {
        List<FieldValue> fieldValueList = new ArrayList<FieldValue>();

        int min = 10;
        Random random = new Random();


        int numberOFField = random.nextInt(ValueType.values().length - min) + min;

        for (int i = 0; i < numberOFField; ++i) {
            int index = random.nextInt(ValueType.values().length - min);
            Field field = new Field();
            field.setName(ValueType.values()[index].toString() + "" + i);


            GreaterThanConstraint greaterThanConstraint = new GreaterThanConstraint(5, true);
            field.getConstraints().add(greaterThanConstraint);
            LessThanConstraint lessThanConstraint = new LessThanConstraint(new Date(), true);
            field.getConstraints().add(lessThanConstraint);
            RangeConstraint rangeThanConstraint = new RangeConstraint(2l, true, 50.4, false);
            field.getConstraints().add(rangeThanConstraint);
            LengthConstraint lengthThanConstraint = new LengthConstraint(100);
            field.getConstraints().add(lengthThanConstraint);
            field.setSearchable(true);


            field.setValueType((ValueType.values()[index]));
            FieldValue fieldValue = new FieldValue();
            fieldValue.setField(field);
            Object dummyObj = DummyValueType.getDummyType(ValueType.values()[index], 1);
            if (dummyObj != null) {
                fieldValue.setValue(dummyObj);
                fieldValueList.add(fieldValue);
            }
        }
        return fieldValueList;
    }


    public static void main(String[] args) {
        PojoFactory pojoFactory = new PojoFactory() {
            @Override
            public IDefinition getNewDefinition() {
                return new JCRDefinition();
            }

            @Override
            public IItem getNewItem() {
                return new JCRItem();
            }

            @Override
            public IUser getNewUser() {
                return new JCRUser();
            }
        };
        IItem<IItem, IDefinition> item = getItemTree(pojoFactory, pojoFactory.getNewDefinition(), 3);

        System.out.println(item);
    }
}
