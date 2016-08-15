package com.sp.validate.constraint;

/**
 * Created by pankajmishra on 06/08/16.
 *
 * Return true for the string which has length as mentioned in constraint.
 * If string in null then length must be 0 for constraint to pass.
 * Note: String is  trimmed for the length purpose.
 */
public class LengthConstraint implements Constraint<String> {

    private int length;

    public LengthConstraint(){

    }

    public LengthConstraint(int length){
        this.length = length;
    }

    @Override
    public boolean pass(String val) {
        if(val == null){
            return length == 0;
        }
        return val.trim().length() == length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LengthConstraint that = (LengthConstraint) o;

        if (length != that.length) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return length;
    }

    @Override
    public String toString() {
        return "LengthConstraint{" +
                "length=" + length +
                '}';
    }
}
