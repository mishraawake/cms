package com.sp.helper;

import com.sp.model.Field;
import com.sp.model.IDefinition;
import com.sp.model.PojoFactory;
import com.sp.model.ValueType;
import com.sp.validate.constraint.GreaterThanConstraint;
import com.sp.validate.constraint.LengthConstraint;
import com.sp.validate.constraint.LessThanConstraint;
import com.sp.validate.constraint.RangeConstraint;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class DefUtils {

    public static IDefinition getDummyDefinition(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName(RandomStringUtils.randomAlphabetic(20));
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());
        for (int i = 0; i < 10; ++i) {
            Field field = new Field();
            field.setName("field" + i);
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


    public static IDefinition getChannel(PojoFactory pojoFactory) {
        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("channel");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Name");
        nameField.setValueType(ValueType.String);
        LengthConstraint lengthThanConstraint = new LengthConstraint(100);
        nameField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(nameField);


        // it should have a description field
        Field descField = new Field();
        descField.setName("Description");
        descField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(descField);


        // it should have an image field
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.Image);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);

        return definition;

    }


    public static IDefinition getSection(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Section");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Name");
        nameField.setValueType(ValueType.String);
        LengthConstraint lengthThanConstraint = new LengthConstraint(100);
        nameField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(nameField);


        // it should have a description field
        Field descField = new Field();
        descField.setName("Description");
        descField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(descField);


        // it should have an image field
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.Image);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);

        return definition;
    }


    public static IDefinition getPoll(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Poll");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Name");
        nameField.setValueType(ValueType.String);
        LengthConstraint lengthThanConstraint = new LengthConstraint(100);
        nameField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(nameField);


        // it should have a description field
        Field descField = new Field();
        descField.setName("Description");
        descField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(descField);

        // it should have a description field
        Field questionField = new Field();
        questionField.setName("Question");
        questionField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(questionField);


        // it should have a description field -- here ideal should be that there is array of definition that will
        // be combination of image and name .. and possibly other field for the answer.
        Field answer = new Field();
        answer.setName("Answer");
        answer.setValueType(ValueType.GENERIC_TYPE);
        lengthThanConstraint = new LengthConstraint(100);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(answer);


        // it should have an image field
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.Image);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);


        return definition;
    }


    public static IDefinition getGallery(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Gallery");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Name");
        nameField.setValueType(ValueType.String);
        LengthConstraint lengthThanConstraint = new LengthConstraint(100);
        nameField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(nameField);


        // it should have a description field
        Field descField = new Field();
        descField.setName("Description");
        descField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(descField);


        // it should have an image field -- here also we need array of definition which will be other detail of image.
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.ArrayOfImage);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);


        return definition;
    }


    public static IDefinition getArticle(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Article");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Name");
        nameField.setValueType(ValueType.String);
        LengthConstraint lengthThanConstraint = new LengthConstraint(100);
        nameField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(nameField);


        // it should have a description field
        Field descField = new Field();
        descField.setName("Description");
        descField.setValueType(ValueType.BigString);
        lengthThanConstraint = new LengthConstraint(1000);
        descField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(descField);


        // it should have an image field -- here also we need array of definition which will be other detail of image.
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.ArrayOfImage);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);

        // it should have an image field -- here also we need array of definition which will be other detail of image.
        Field synopsys = new Field();
        synopsys.setName("Body");
        synopsys.setValueType(ValueType.RichMediaText);
        definition.getFields().add(synopsys);


        // it should have an image field -- here also we need array of definition which will be other detail of image.
        Field body = new Field();
        body.setName("Synosys");
        body.setValueType(ValueType.BigString);
        definition.getFields().add(body);

        return definition;
    }


}
