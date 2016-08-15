package com.sp.utils;

import com.sp.model.Field;
import com.sp.model.ValueType;
import com.sp.model.IDefinition;
import com.sp.model.PojoFactory;
import com.sp.validate.constraint.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class DefUtils {

    public static IDefinition getDummyDefition(PojoFactory pojoFactory){

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName(RandomStringUtils.randomAlphabetic(20));
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));

        for(int i=0; i<10; ++i){
            Field field = new Field();
            field.setName("field"+i);
            GreaterThanConstraint greaterThanConstraint = new GreaterThanConstraint(5, true);
            field.getConstraints().add(greaterThanConstraint);
            LessThanConstraint lessThanConstraint = new LessThanConstraint(new Date(), true);
            field.getConstraints().add(lessThanConstraint);
            RangeConstraint rangeThanConstraint = new RangeConstraint(2l, true, 50.4, false);
            field.getConstraints().add(rangeThanConstraint);
            LengthConstraint lengthThanConstraint = new LengthConstraint(100);
            field.getConstraints().add(lengthThanConstraint);

            field.setSearchable(true);
            field.setValueType(ValueType.values()[i]);
            definition.getFields().add(field);
        }

        return definition;
    }
}
