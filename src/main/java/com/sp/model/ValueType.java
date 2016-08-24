package com.sp.model;

/**
 * Created by pankajmishra on 06/08/16.
 * <p>
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
    String {
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfString(true){
        public boolean isStringType(){
            return true;
        }
    },
    /**
     * for one which will require a textbox
     */
    BigString{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfBigString(true){
        public boolean isStringType(){
            return true;
        }
    },
    /**
     * For one which may contain rich text. html, anchor etc.
     */
    RichText{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfRichText(true){
        public boolean isStringType(){
            return true;
        }
    },
    /**
     * It will contain all the html feature along with embedding the media element such as image, video etc.
     */
    RichMediaText{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfRichMediaText(true){
        public boolean isStringType(){
            return true;
        }
    },
    /** **/
    Image{
        public boolean isBinaryType(){
            return true;
        }
    },
    ArrayOfImage(true){
        public boolean isBinaryType(){
            return true;
        }
    },
    Video{
        public boolean isBinaryType(){
            return true;
        }
    },
    ArrayOfVideo(true){
        public boolean isBinaryType(){
            return true;
        }
    },
    File{
        public boolean isBinaryType(){
            return true;
        }
    },
    ArrayOfFile(true){
        public boolean isBinaryType(){
            return true;
        }
    },
    Time,
    ArrayOfTime(true),
    Enum{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfEnum(true){
        public boolean isStringType(){
            return true;
        }
    },
    Ref,
    ArrayOfRef(true),
    Phone{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfPhone(true){
        public boolean isStringType(){
            return true;
        }
    },
    Email{
        public boolean isStringType(){
            return true;
        }
    },
    ArrayOfEmail(true){
        public boolean isStringType(){
            return true;
        }
    },
    Definition,
    ArrayOfDefinition(true);

    private ValueType() {
    }

    private ValueType(Boolean array) {
        this.array = array;
    }


    private boolean array = false;

    public boolean isArray() {
        return array;
    }

    public boolean isDefinition(){
        return this.equals(Definition) || this.equals(ArrayOfDefinition);
    }

    public boolean isStringType(){
        return false;
    }

    public boolean isBinaryType(){
        return false;
    }

    @Override
    public String toString() {
        return name();
    }


}
