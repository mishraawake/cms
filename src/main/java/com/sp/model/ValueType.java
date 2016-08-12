package com.sp.model;

/**
 * Created by pankajmishra on 06/08/16.
 * <p/>
 * This value type is more for the help of building form to get input from cms for the definition.
 */
public enum ValueType {

    Boolean("boolean"),
    ArrayOfBoolean(true, "boolean"),
    Byte("byte"),
    ArrayOfByte(true, "byte"),
    Char("char"),
    ArrayOfChar(true, "char"),
    Short("short"),
    ArrayOfShort(true, "short"),
    Integer("integer"),
    ArrayOfInteger(true, "integer"),
    Long("long"),
    ArrayOfLong(true, "long"),
    Double("double"),
    ArrayOfDouble(true, "double"),
    Float("float"),
    ArrayOfFloat(true, "float"),
    Date("date"),
    ArrayOfDate(true, "date"),
    String("string"),
    ArrayOfString(true, "string"),
    /**
     * for one whih will require a textbox
     */
    BigString("bigstring"),
    ArrayOfBigString(true, "bigstring"),
    /**
     * For one which may contain rich text. html, anchor etc.
     */
    RichText("richtext"),
    ArrayOfRichText(true, "richtext"),
    /**
     * It will contain all the html feature along with embedding the media element such as image, video etc.
     */
    RichMediaText("richmediatext"),
    ArrayOfRichMediaText(true, "richmediatext"),
    /** **/
    Image("image"),
    ArrayOfImage(true, "image"),
    Video("video"),
    ArrayOfVideo(true, "video"),
    File("file"),
    ArrayOfFile(true, "file"),
    Time("time"),
    ArrayOfTime(true, "time"),
    Enum("enum"),
    ArrayOfEnum(true, "enum"),
    Ref("ref"),
    ArrayOfRef(true, "ref"),
    Phone("phone"),
    ArrayOfPhone(true, "phone"),
    Email("email"),
    ArrayOfEmail(true, "email"),
    UserGenerated("valueType"),

    ArrayOfUserGenerated(true, "valueType");

    private ValueType(String value) {
        this.value = value;
    }

    private ValueType(Boolean array, String value) {
        this.array = array;
        this.value = value;
    }

    private String value;

    private boolean array = false;

    public String value() {
        return value;
    }

    public boolean array() {
        return array;
    }


    @Override
    public String toString() {
        return value;
    }
}
