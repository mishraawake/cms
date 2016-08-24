package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pankajmishra on 06/08/16.
 * <p>
 * Return true for the string which has length as mentioned in constraint.
 * If string in null then length must be 0 for constraint to pass.
 * Note: String is  trimmed for the length purpose.
 */
public class LengthConstraint implements Constraint<String> {

    private int length;

    private String errorMessage;

    private final String DEFAULT_ERROR_MESSAGE = "Supplied value Length should be %s";

    @JsonCreator
    public LengthConstraint(@JsonProperty("length") int length) {
        this.length = length;
        errorMessage = String.format(DEFAULT_ERROR_MESSAGE, length);
    }

    @Override
    public boolean pass(String val) {
        if (val == null) {
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
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
