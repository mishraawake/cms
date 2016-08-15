package com.sp.model;

/**
 * Created by pankajmishra on 06/08/16.
 * <p/>
 * This value type is more for the help of building form to get input from cms for the definition.
 */
public enum ValueType {

    Boolean(ValueType.TYPE_BOOLEAN),
    ArrayOfBoolean(true, ValueType.TYPE_BOOLEAN),
    Byte(ValueType.TYPE_BYTE),
    ArrayOfByte(true, ValueType.TYPE_BYTE),
    Char(ValueType.TYPE_CHAR),
    ArrayOfChar(true, ValueType.TYPE_CHAR),
    Short(ValueType.TYPE_SHORT),
    ArrayOfShort(true, ValueType.TYPE_SHORT),
    Integer(ValueType.TYPE_INTEGER),
    ArrayOfInteger(true, ValueType.TYPE_INTEGER),
    Long(ValueType.TYPE_LONG),
    ArrayOfLong(true, ValueType.TYPE_LONG),
    Double(ValueType.TYPE_DOUBLE),
    ArrayOfDouble(true, ValueType.TYPE_DOUBLE),
    Float(ValueType.TYPE_FLOAT),
    ArrayOfFloat(true, ValueType.TYPE_FLOAT),
    Date(ValueType.TYPE_DATE),
    ArrayOfDate(true, ValueType.TYPE_DATE),
    String(ValueType.TYPE_STRING),
    ArrayOfString(true, ValueType.TYPE_STRING),
    /**
     * for one which will require a textbox
     */
    BigString(ValueType.TYPE_BIG_STRING),
    ArrayOfBigString(true, ValueType.TYPE_BIG_STRING),
    /**
     * For one which may contain rich text. html, anchor etc.
     */
    RichText(ValueType.TYPE_RICH_TEXT),
    ArrayOfRichText(true, ValueType.TYPE_RICH_TEXT),
    /**
     * It will contain all the html feature along with embedding the media element such as image, video etc.
     */
    RichMediaText(ValueType.TYPE_RICH_MEDIA_TEXT),
    ArrayOfRichMediaText(true, ValueType.TYPE_RICH_MEDIA_TEXT),
    /** **/
    Image(ValueType.TYPE_IMAGE),
    ArrayOfImage(true, ValueType.TYPE_IMAGE),
    Video(ValueType.TYPE_VIDEO),
    ArrayOfVideo(true, ValueType.TYPE_VIDEO),
    File(ValueType.TYPE_FILE),
    ArrayOfFile(true, ValueType.TYPE_FILE),
    Time(ValueType.TYPE_TIME),
    ArrayOfTime(true, ValueType.TYPE_TIME),
    Enum(ValueType.TYPE_ENUM),
    ArrayOfEnum(true, ValueType.TYPE_ENUM),
    Ref(ValueType.TYPE_REF),
    ArrayOfRef(true, ValueType.TYPE_REF),
    Phone(ValueType.TYPE_PHONE),
    ArrayOfPhone(true, ValueType.TYPE_PHONE),
    Email(ValueType.TYPE_EMAIL),
    ArrayOfEmail(true, ValueType.TYPE_EMAIL),
    GENERIC_TYPE(ValueType.TYPE_GENERIC_TYPE);

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



    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_BYTE = "byte";
    public static final String TYPE_CHAR = "char";
    public static final String TYPE_SHORT = "short";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_BIG_STRING = "bigstring";
    public static final String TYPE_RICH_TEXT = "richtext";
    public static final String TYPE_RICH_MEDIA_TEXT = "richmediatext";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_TIME = "time";
    public static final String TYPE_ENUM = "enum";
    public static final String TYPE_REF = "ref";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_GENERIC_TYPE = "generic";


}
