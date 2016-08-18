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
                return random.nextInt();
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
            case GENERIC_TYPE:
                FieldValue[] fieldValueList = ItemUtils.getDummyFieldValueList(depth - 1).toArray(new FieldValue[0]);
                return fieldValueList;
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
