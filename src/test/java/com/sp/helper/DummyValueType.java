package com.sp.helper;

import com.sp.model.*;
import com.sp.validate.constraint.Constraint;
import com.sp.validate.constraint.GreaterThanConstraint;
import com.sp.validate.constraint.LessThanConstraint;
import com.sp.validate.constraint.RangeConstraint;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by pankajmishra on 08/08/16.
 */
public class DummyValueType {


    public static Object getDummyType(ValueType type, int depth) {

        Random random = new Random();
        switch (type) {
            case ArrayOfBoolean:
                return new boolean[]{true, false};
            case Boolean:
                return true;
            case ArrayOfByte:
                return new byte[]{(byte) 10, (byte) 11};
            case Byte:
                return (byte) 10;
            case ArrayOfChar:
                return new char[]{'1', '2'};
            case Char:
                return 'a';
            case ArrayOfShort:
                return new short[]{(short) 10, (short) 11};
            case Short:
                return (short) 10;
            case ArrayOfInteger:
                return new int[]{11, 12};
            case Integer:
                return 20;
            case ArrayOfLong:
                return new long[]{19l, 30l};
            case Long:
                return random.nextLong();
            case ArrayOfDouble:
                return new double[]{random.nextDouble(), random.nextDouble()};
            case Double:
                return random.nextDouble();
            case ArrayOfFloat:
                return new float[]{random.nextFloat(), random.nextFloat()};
            case Float:
                return random.nextFloat();
            case ArrayOfDate:
                return new Date[]{new Date(), new Date()};
            case Date:
                return new Date();
            case ArrayOfString:
                return new String[]{RandomStringUtils.randomAlphabetic(50), RandomStringUtils.randomAlphabetic(50)};
            case String:
                return RandomStringUtils.randomAlphabetic(50);
            case ArrayOfBigString:
                return new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
            case BigString:
                return RandomStringUtils.randomAlphabetic(500);
            case ArrayOfRichText:
                return new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
            case RichText:
                return RandomStringUtils.randomAlphabetic(500);
            case ArrayOfRichMediaText:
                return new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
            case RichMediaText:
                return RandomStringUtils.randomAlphabetic(500);
            case ArrayOfImage:
                return new Image[]{getRandomImage(), getRandomImage()};
            case Image:
                return getRandomImage();
            case ArrayOfVideo:
                return new Video[]{getRandomVideo(), getRandomVideo()};
            case Video:
                return getRandomVideo();
            case ArrayOfFile:
                return new FileObject[]{getRandomFile(), getRandomFile()};
            case File:
                return getRandomFile();
            case ArrayOfTime:
                return new double[]{random.nextDouble(), random.nextDouble()};
            case Time:
                return random.nextDouble();
            case ArrayOfEnum:
                return new String[]{"still to test", "still to test"};
            case Enum:
                return "still to test";
            case ArrayOfRef:
                return null;
            case ArrayOfPhone:
                return new String[]{"9711391354", "9711391355"};
            case Phone:
                return "9711391354";
            case ArrayOfEmail:
                return new String[]{"mishraawake@gmail.com", "mishraawake@gmail1.com"};
            case Email:
                return "mishraawake@gmail.com";
            case Definition:
                FieldValue[] fieldValueList = ItemUtils.getDummyFieldValueList(depth - 1).toArray(new FieldValue[0]);
                return fieldValueList;

            case ArrayOfDefinition:
                List<FieldValue[]> listOfArray = new ArrayList<>();
                fieldValueList = ItemUtils.getDummyFieldValueList(depth - 1).toArray(new FieldValue[0]);
                listOfArray.add(fieldValueList);
                fieldValueList = ItemUtils.getDummyFieldValueList(depth - 1).toArray(new FieldValue[0]);
                listOfArray.add(fieldValueList);
                return listOfArray.toArray(new FieldValue[0][0]);
        }

        return null;
    }





    public static List<Constraint> getDummyConstraintBasedOnType(ValueType type) {

        Random random = new Random();
        switch (type) {
            case ArrayOfBoolean:
                return new ArrayList<>();
            case Boolean:
                return new ArrayList<>();
            case ArrayOfByte:
                return new ArrayList<>();
            case Byte:
                return new ArrayList<>();
            case ArrayOfChar:
                return new ArrayList<>();
            case Char:
                return new ArrayList<>();
            case ArrayOfShort:
                return new ArrayList<>();
            case Short:
                return new ArrayList<>();
            case ArrayOfInteger:
                return new ArrayList<>();
            case Integer: {
                List<Constraint> constraintList = new ArrayList<>();
                Constraint constraint = new LessThanConstraint(20, true);
                constraintList.add(constraint);
                constraint = new GreaterThanConstraint(20, true);
                constraintList.add(constraint);
                constraint = new RangeConstraint(20, true, 100, false);
                constraintList.add(constraint);
                return constraintList;
            }
            case ArrayOfLong:
                return new ArrayList<>();
            case Long:
                return new ArrayList<>();
            case ArrayOfDouble:
                return new ArrayList<>();
            case Double:
                return new ArrayList<>();
            case ArrayOfFloat:
                return new ArrayList<>();
            case Float:
                return new ArrayList<>();
            case ArrayOfDate:
                return new ArrayList<>();
            case Date:
                return new ArrayList<>();
            case ArrayOfString:
                return new ArrayList<>();
            case String:
                return new ArrayList<>();
            case ArrayOfBigString:
                return new ArrayList<>();
            case BigString:
                return new ArrayList<>();
            case ArrayOfRichText:
                return new ArrayList<>();
            case RichText:
                return new ArrayList<>();
            case ArrayOfRichMediaText:
                return new ArrayList<>();
            case RichMediaText:
                return new ArrayList<>();
            case ArrayOfImage:
                return new ArrayList<>();
            case Image:
                return new ArrayList<>();
            case ArrayOfVideo:
                return new ArrayList<>();
            case Video:
                return new ArrayList<>();
            case ArrayOfFile:
                return new ArrayList<>();
            case File:
                return new ArrayList<>();
            case ArrayOfTime:
                return new ArrayList<>();
            case Time:
                return new ArrayList<>();
            case ArrayOfEnum:
                return new ArrayList<>();
            case Enum:
                return new ArrayList<>();
            case ArrayOfRef:
                return null;
            case ArrayOfPhone:
                return new ArrayList<>();
            case Phone:
                return new ArrayList<>();
            case ArrayOfEmail:
                return new ArrayList<>();
            case Email:
                return new ArrayList<>();
            case Definition:
                return new ArrayList<>();

            case ArrayOfDefinition:
                return new ArrayList<>();
        }

        return null;
    }


    public static Image getRandomImage() {
        File file = new File("dummy-data/image.jpg");
        Image image = new Image();

        try {
            image.inputStream(new FileInputStream(file));
            image.mimeType("image/jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Video getRandomVideo() {
        File file = new File("dummy-data/video.mp4");

        Video video = new Video();
        video.mimeType("video/mp4");
        try {
            video.inputStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return video;
    }

    public static FileObject getRandomFile() {
        File file = new File("dummy-data/file.pdf");

        FileObject fileObject = new FileObject();
        fileObject.mimeType("file/pdf");
        try {
            fileObject.inputStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileObject;
    }


}
