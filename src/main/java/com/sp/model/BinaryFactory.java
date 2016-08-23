package com.sp.model;

/**
 * Created by pankajmishra on 11/08/16.
 */
public class BinaryFactory {


    public static final BinaryFactory IMAGE_FACTORY = new BinaryFactory("image");

    public static final BinaryFactory VIDEO_FACTORY = new BinaryFactory("video");

    public static final BinaryFactory FILE_FACTORY = new BinaryFactory("file");

    String type;

    private BinaryFactory(String type) {
        this.type = type;
    }

    public BinaryData getObject() {
        if (type.equals("image")) {
            return new Image();
        }
        if (type.equals("video")) {
            return new Video();
        }
        return new FileObject();
    }
}
