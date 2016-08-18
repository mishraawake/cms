package com.sp.model;

/**
 * Created by pankajmishra on 06/08/16.
 * <p/>
 * This value type is more for the help of building form to get input from cms for the definition.
 */
public enum ValueType {

    Boolean,
    ArrayOfBoolean(true),
    Byte,
    ArrayOfByte(true),
    Char,
    ArrayOfChar(true),
    Short,
    ArrayOfShort(true),
    Integer,
    ArrayOfInteger(true),
    Long,
    ArrayOfLong(true),
    Double,
    ArrayOfDouble(true),
    Float,
    ArrayOfFloat(true),
    Date,
    ArrayOfDate(true),
    String,
    ArrayOfString(true),
    /**
     * for one which will require a textbox
     */
    BigString,
    ArrayOfBigString(true),
    /**
     * For one which may contain rich text. html, anchor etc.
     */
    RichText,
    ArrayOfRichText(true),
    /**
     * It will contain all the html feature along with embedding the media element such as image, video etc.
     */
    RichMediaText,
    ArrayOfRichMediaText(true),
    /** **/
    Image,
    ArrayOfImage(true),
    Video,
    ArrayOfVideo(true),
    File,
    ArrayOfFile(true),
    Time,
    ArrayOfTime(true),
    Enum,
    ArrayOfEnum(true),
    Ref,
    ArrayOfRef(true),
    Phone,
    ArrayOfPhone(true),
    Email,
    ArrayOfEmail(true),
    GENERIC_TYPE;

    private ValueType() {
    }

    private ValueType(Boolean array) {
        this.array = array;
    }


    private boolean array = false;

    public String value() {
        return name();
    }

    public boolean array() {
        return array;
    }


    @Override
    public String toString() {
        return name();
    }



}
