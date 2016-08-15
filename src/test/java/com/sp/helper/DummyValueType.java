package com.sp.helper;

import com.sp.model.*;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;

/**
 * Created by pankajmishra on 08/08/16.
 */
public class DummyValueType {


    public static Object getDummyType(ValueType type, int depth){

        Random random = new Random();
        switch (type.value()){
            case ValueType.TYPE_BOOLEAN:
                if(type.array()){
                    return  new boolean[]{true, false};
                }
                return true;
            case ValueType.TYPE_BYTE:
                if(type.array()){
                    return  new byte[]{ (byte)10, (byte)11};
                }
                return  (byte)10;

            case ValueType.TYPE_CHAR:
                if(type.array()){
                    return  new char[]{'1', '2'};
                }
                return 'a';
            case ValueType.TYPE_SHORT:
                if(type.array()){
                    return  new short[]{ (short)10, (short)11};
                }
                return  (short)10;
            case ValueType.TYPE_INTEGER:
                if(type.array()){
                    return new int[]{11, 12};
                }
                return random.nextInt();
            case ValueType.TYPE_LONG:
                if(type.array()){
                    return  new long[]{19l, 30l};
                }
                return random.nextLong();
            case ValueType.TYPE_DOUBLE:
                if(type.array()){
                    return  new double[]{random.nextDouble(), random.nextDouble()};
                }
                return random.nextDouble();
            case ValueType.TYPE_FLOAT:
                if(type.array()){
                    return  new float[]{random.nextFloat(), random.nextFloat()};
                }
                return random.nextFloat();
            case ValueType.TYPE_DATE:
                if(type.array()){
                    return  new Date[]{new Date(), new Date()};
                }
                return new Date();
            case ValueType.TYPE_STRING:
                if(type.array()){
                    return  new String[]{RandomStringUtils.randomAlphabetic(50), RandomStringUtils.randomAlphabetic(50)};
                }
                return RandomStringUtils.randomAlphabetic(50);
            case ValueType.TYPE_BIG_STRING:
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }
                return RandomStringUtils.randomAlphabetic(500);
            case ValueType.TYPE_RICH_TEXT:
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }

                return RandomStringUtils.randomAlphabetic(500);
            case ValueType.TYPE_RICH_MEDIA_TEXT:
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }
                return RandomStringUtils.randomAlphabetic(500);
            case ValueType.TYPE_IMAGE:
                if(type.array()){
                    return  new Image[]{ getRandomImage(),  getRandomImage()};
                }
                return getRandomImage();
            case ValueType.TYPE_VIDEO:
                if(type.array()){
                    return  new Video[]{ getRandomVideo(),  getRandomVideo()};
                }
               return getRandomVideo();
            case ValueType.TYPE_FILE:
                if(type.array()){
                    return  new FileObject[]{ getRandomFile(),  getRandomFile()};
                }
                return getRandomFile();
            case ValueType.TYPE_TIME:
                if(type.array()){
                    return  new double[]{ random.nextDouble(),  random.nextDouble()};
                }
                return random.nextDouble();
            case ValueType.TYPE_ENUM:
                if(type.array()){
                    return  new String[]{ "still to test",  "still to test"};
                }
                return "still to test";
            case ValueType.TYPE_REF:
                return null;
            case ValueType.TYPE_PHONE:
                if(type.array()){
                    return  new String[]{ "9711391354",  "9711391355"};
                }
                return "9711391354";
            case ValueType.TYPE_EMAIL:
                if(type.array()){
                    return  new String[]{ "mishraawake@gmail.com",  "mishraawake@gmail1.com"};
                }
                return "mishraawake@gmail.com";
            case ValueType.TYPE_GENERIC_TYPE:
                FieldValue[] fieldValueList = ItemUtils.getDummyFieldValueList(depth - 1).toArray(new FieldValue[0]);
                return fieldValueList;
        }

        return null;
    }


    public static Image getRandomImage(){
        File file = new File("dummy-data/image.jpg");
        Image image = new Image();

        try {
            image.inputStream(new FileInputStream(file));
            image.mimeType("image/jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Video getRandomVideo(){
        File file = new File("dummy-data/video.mp4");

        Video video = new Video();
        video.mimeType("video/mp4");
        try {
            video.inputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return video;
    }

    public static FileObject getRandomFile(){
        File file = new File("dummy-data/file.pdf");

        FileObject fileObject = new FileObject();
        fileObject.mimeType("file/pdf");
        try {
            fileObject.inputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileObject;
    }


}
