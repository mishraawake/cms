package com.sp.utils;

import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.model.ValueType;
import com.sp.model.IItem;
import com.sp.model.PojoFactory;
import com.sp.validate.constraint.GreaterThanConstraint;
import com.sp.validate.constraint.LengthConstraint;
import com.sp.validate.constraint.LessThanContraint;
import com.sp.validate.constraint.RangeConstraint;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

/**
 * Created by pankajmishra on 08/08/16.
 */
public class ItemUtils {

    public static IItem getDummyItem(PojoFactory pojoFactory){

        IItem item = pojoFactory.getNewItem();
        item.setName(RandomStringUtils.randomAlphabetic(50));
        for(int i=0; i < 44; ++i){
            Field field = new Field();
            field.setName(ValueType.values()[i].toString()+""+i);
            GreaterThanConstraint greaterThanConstraint = new GreaterThanConstraint(5, true);
            field.getConstraints().add(greaterThanConstraint);
            LessThanContraint lessThanConstraint = new LessThanContraint(new Date(), true);
            field.getConstraints().add(lessThanConstraint);
            RangeConstraint rangeThanConstraint = new RangeConstraint(new Long(2), true, 50.4, false);
            field.getConstraints().add(rangeThanConstraint);
            LengthConstraint lengthThanConstraint = new LengthConstraint(new Integer(100));
            field.getConstraints().add(lengthThanConstraint);

            field.setSearchable(true);
            field.setValueType((ValueType.values()[i]));

            FieldValue fieldValue = new FieldValue();
            fieldValue.setField(field);
            fieldValue.setValue(DummyValueType.getDummyType(ValueType.values()[i]));
            item.getFieldValues().add(fieldValue);
        }

        return item;
    }
}
