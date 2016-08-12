package com.sp.utils;

import com.sp.model.ValueType;
import com.sp.model.FileObject;
import com.sp.model.Image;
import com.sp.model.Video;
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

    public static Object getDummyType(ValueType type){

        Random random = new Random();
        switch (type.value()){
            case "boolean":
                if(type.array()){
                    return  new Boolean[]{true, false};
                }
                return true;
            case "byte":
                if(type.array()){
                    return  new Byte[]{Byte.valueOf("10"), Byte.valueOf("11")};
                }
                return  Byte.valueOf("10");

            case "char":
                if(type.array()){
                    return  new Character[]{'1', '2'};
                }
                return 'a';
            case "short":
                if(type.array()){
                    return new Short[]{Short.valueOf("10"), Short.valueOf("11")};
                }
                return Short.valueOf("10");
            case "integer":
                if(type.array()){
                    return new Integer[]{11, 12};
                }
                return random.nextInt();
            case "long":
                if(type.array()){
                    return  new Long[]{19l, 30l};
                }
                return random.nextLong();
            case "double":
                if(type.array()){
                    return  new Double[]{random.nextDouble(), random.nextDouble()};
                }
                return random.nextDouble();
            case "float":
                if(type.array()){
                    return  new Float[]{random.nextFloat(), random.nextFloat()};
                }
                return random.nextFloat();
            case "date":
                if(type.array()){
                    return  new Date[]{new Date(), new Date()};
                }
                return new Date();
            case "string":
                if(type.array()){
                    return  new String[]{RandomStringUtils.randomAlphabetic(50), RandomStringUtils.randomAlphabetic(50)};
                }
                return RandomStringUtils.randomAlphabetic(50);
            case "bigstring":
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }
                return RandomStringUtils.randomAlphabetic(500);
            case "richtext":
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }

                return RandomStringUtils.randomAlphabetic(500);
            case "richmediatext":
                if(type.array()){
                    return   new String[]{RandomStringUtils.randomAlphabetic(500), RandomStringUtils.randomAlphabetic(500)};
                }
                return RandomStringUtils.randomAlphabetic(500);
            case "image":
                if(type.array()){
                    return  new Image[]{ getRandomImage(),  getRandomImage()};
                }
                return getRandomImage();
            case "video":
                if(type.array()){
                    return  new Video[]{ getRandomVideo(),  getRandomVideo()};
                }
               return getRandomVideo();
            case "file":
                if(type.array()){
                    return  new FileObject[]{ getRandomFile(),  getRandomFile()};
                }
                return getRandomFile();
            case "time":
                if(type.array()){
                    return  new Double[]{ random.nextDouble(),  random.nextDouble()};
                }
                return random.nextDouble();
            case "enum":
                if(type.array()){
                    return  new String[]{ "still to test",  "still to test"};
                }
                return "still to test";
            case "ref":
                if(type.array()){
                    return  new Object[]{ null,  null};
                }
                return null;
            case "phone":
                if(type.array()){
                    return  new String[]{ "9711391354",  "9711391355"};
                }
                return "9711391354";
            case "email":
                if(type.array()){
                    return  new String[]{ "mishraawake@gmail.com",  "mishraawake@gmail1.com"};
                }
                return "mishraawake@gmail.com";
            case "usergenerated":
                if(type.array()){
                    return  new String[]{ "still to test",  "still to test"};
                }
                return "still to test";
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
