package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pankajmishra on 24/08/16.
 */
public class ValueConstraint<T> implements Constraint<T> {

    T targetValue;

    private String errorMessage;

    private final String DEFAULT_ERROR_MESSAGE = "Supplied value should exactly be %s";

    @JsonCreator
    public ValueConstraint(@JsonProperty("targetValue") T targetValue){
        this.targetValue = targetValue;
        errorMessage = String.format(DEFAULT_ERROR_MESSAGE, targetValue);
    }

    @Override
    public boolean pass(T value) {
        return targetValue.equals(value);
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueConstraint)) return false;

        ValueConstraint that = (ValueConstraint) o;

        if (targetValue != null ? !targetValue.equals(that.targetValue) : that.targetValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return targetValue != null ? targetValue.hashCode() : 0;
    }
}
