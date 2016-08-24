package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class LessThanConstraint implements Constraint<Comparable> {

    Comparable from;

    boolean eq;

    private String errorMessage;

    private final String DEFAULT_ERROR_MESSAGE = "Supplied value should less than %s.. mind the " +
            "inclusiveness " +
            "boolean eq = %s !";

    @JsonCreator
    public LessThanConstraint(@JsonProperty("from") Comparable from, @JsonProperty("eq") boolean eq) {
        this.from = from;
        this.eq = eq;
        errorMessage = String.format(DEFAULT_ERROR_MESSAGE, from, eq);
    }


    @Override
    public boolean pass(Comparable value) {
        return eq ? from.compareTo(value) >= 0 : from.compareTo(value) > 0;
    }

    public Comparable getFrom() {
        return from;
    }

    public void setFrom(Comparable from) {
        this.from = from;
    }

    public boolean isEq() {
        return eq;
    }

    public void setEq(boolean eq) {
        this.eq = eq;
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
        if (o == null || getClass() != o.getClass()) return false;

        LessThanConstraint that = (LessThanConstraint) o;

        if (eq != that.eq) return false;
        if (from != null ? !from.equals(that.from) : that.from != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (eq ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessThanContraint{" +
                "from=" + from +
                ", eq=" + eq +
                '}';
    }
}
