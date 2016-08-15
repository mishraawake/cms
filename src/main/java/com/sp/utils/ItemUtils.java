package com.sp.utils;

import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.model.ValueType;
import com.sp.model.IItem;
import com.sp.model.PojoFactory;
import com.sp.validate.constraint.GreaterThanConstraint;
import com.sp.validate.constraint.LengthConstraint;
import com.sp.validate.constraint.LessThanConstraint;
import com.sp.validate.constraint.RangeConstraint;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 08/08/16.
 */
public class ItemUtils {

    public static IItem getDummyItem(PojoFactory pojoFactory){

        IItem item = pojoFactory.getNewItem();
        item.setName(RandomStringUtils.randomAlphabetic(50));
        item.setFieldValues(getDummyFieldValueList(1));
        return item;
    }


    public static List<FieldValue> getDummyFieldValueList(int depth){
        if(depth == 0){
            return new ArrayList<FieldValue>();
        }
        List<FieldValue> fieldValueList = new ArrayList<FieldValue>();
        for(int i=0; i < ValueType.values().length; ++i){
            Field field = new Field();
            field.setName(ValueType.values()[i].toString()+""+i);
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
            FieldValue fieldValue = new FieldValue();
            fieldValue.setField(field);
            Object dummyObj = DummyValueType.getDummyType(ValueType.values()[i], depth);
            if(dummyObj != null){
                fieldValue.setValue(dummyObj);
                fieldValueList.add(fieldValue);
            }

        }
        return fieldValueList;
    }
}
