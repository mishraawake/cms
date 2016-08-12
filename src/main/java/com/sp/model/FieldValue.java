package com.sp.model;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class FieldValue {

    private Field field;
    private Object value;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static FieldValue getFieldValue(Field field, Object value){
        FieldValue fieldValue = new FieldValue();
        fieldValue.value = value;
        fieldValue.field = field;
        return fieldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldValue that = (FieldValue) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldValue{" +
                "field=" + field +
                ", value=" + value +
                '}';
    }
}
