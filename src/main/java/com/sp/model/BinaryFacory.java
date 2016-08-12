package com.sp.model;

/**
 * Created by pankajmishra on 11/08/16.
 */
public class BinaryFacory {


    public static BinaryFacory IMAGE_FACTORY = new BinaryFacory("image");

    public static BinaryFacory VIDEO_FACTORY = new BinaryFacory("image");

    public static BinaryFacory FILE_FACTORY = new BinaryFacory("image");

    String type;

    private BinaryFacory(String type){
        this.type = type;
    }

    public BinaryData getObject(){
        Object x = Image.class;
        if( type.equals("image") ){
            return new Image();
        }
        if(type.equals("video")){
            return new Video();
        }
        return new FileObject();
    }
}
