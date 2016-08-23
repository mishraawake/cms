package com.sp.helper;

import com.sp.model.*;
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
        for (int i = 0; i < ValueType.values().length; ++i) {

            Field field = null;
            if(! (ValueType.values()[i].isDefinition()) ){
                field = new Field();
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
            } else {
                field = new DefinitionField();
                field.setName("field" + i);
                field.setSearchable(true);
                field.setValueType(ValueType.values()[i]);
                ((DefinitionField)field).setDefinition(getQuestionDefinition(pojoFactory, 4) );
            }

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



    public static IDefinition getReversePoll(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Poll");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());


        // it should have a description field
        DefinitionField questionField = new DefinitionField();
        questionField.setName("Question");
        questionField.setValueType(ValueType.Definition);
        questionField.setDefinition(getQuestionDefinition(pojoFactory, 3));
        definition.getFields().add(questionField);

        // it should have a description field
        DefinitionField answer = new DefinitionField();
        answer.setName("Answer");
        answer.setValueType(ValueType.Definition);
        answer.setDefinition(getQuestionDefinition(pojoFactory, 4));
        definition.getFields().add(answer);

        return definition;
    }



    public static IDefinition getPoll(PojoFactory pojoFactory) {

        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Poll"+RandomStringUtils.randomAlphabetic(5));
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
        DefinitionField questionField = new DefinitionField();
        questionField.setName("Question");
        questionField.setValueType(ValueType.Definition);
        questionField.setDefinition(getQuestionDefinition(pojoFactory, 4));
        definition.getFields().add(questionField);


        // it should have an image field
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.Image);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);


        return definition;
    }


    private static IDefinition getQuestionDefinition(PojoFactory pojoFactory, int numberOfAnswer){
        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Question");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Title");
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


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.Image);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
        DefinitionField answerOptions = new DefinitionField();
        answerOptions.setName("AnswerOption");
        answerOptions.setArraySize(numberOfAnswer);
        answerOptions.setDefinition(getAnswerDefinition(pojoFactory));
        answerOptions.setValueType(ValueType.ArrayOfDefinition);
        definition.getFields().add(answerOptions);

        return definition;
    }



    private static IDefinition getAnswerDefinition(PojoFactory pojoFactory){
        IDefinition definition = pojoFactory.getNewDefinition();

        definition.setName("Answer");
        definition.setDescription(RandomStringUtils.randomAlphabetic(100));
        definition.setCreateDate(new Date());

        // it should have a name field
        Field nameField = new Field();
        nameField.setName("Title");
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


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
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


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
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


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
        Field imageField = new Field();
        imageField.setName("Image");
        imageField.setValueType(ValueType.ArrayOfImage);
        lengthThanConstraint = new LengthConstraint(100000);
        imageField.getConstraints().add(lengthThanConstraint);
        definition.getFields().add(imageField);

        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
        Field synopsys = new Field();
        synopsys.setName("Body");
        synopsys.setValueType(ValueType.RichMediaText);
        definition.getFields().add(synopsys);


        // it should have an image field -- here also we need isArray of definition which will be other detail of image.
        Field body = new Field();
        body.setName("Synosys");
        body.setValueType(ValueType.BigString);
        definition.getFields().add(body);

        return definition;
    }


}
