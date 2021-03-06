package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class RangeConstraint implements Constraint<Comparable> {

    Comparable start;

    Comparable end;

    boolean inst;

    boolean inen;

    private final String DEFAULT_ERROR_MESSAGE = "Supplied value should lie between %s, %s.. mind the inclusiveness " +
            "boolean include start '%s' and include end '%s' !";

    private String errorMessage;

    @JsonCreator
    public RangeConstraint(@JsonProperty("start") Comparable start,@JsonProperty("inst") boolean inst, @JsonProperty
            ("end") Comparable end, @JsonProperty("inen") boolean inen) {
        this.start = start;
        this.inst = inst;
        this.end = end;
        this.inen = inen;
        errorMessage = String.format(DEFAULT_ERROR_MESSAGE, start, end, inst, inen);
    }


    @Override
    public boolean pass(Comparable value) {

        boolean satisfystart = false;

        boolean satisfyend = false;

        satisfystart = inst ? start.compareTo(value) <= 0 :
                start.compareTo(value) < 0;

        satisfyend = inen ? value.compareTo(end) <= 0 :
                value.compareTo(end) < 0;

        return satisfystart && satisfyend;

    }


    public Comparable getStart() {
        return start;
    }

    public void setStart(Comparable start) {
        this.start = start;
    }

    public Comparable getEnd() {
        return end;
    }

    public void setEnd(Comparable end) {
        this.end = end;
    }

    public boolean isInst() {
        return inst;
    }

    public void setInst(boolean inst) {
        this.inst = inst;
    }

    public boolean isInen() {
        return inen;
    }

    public void setInen(boolean inen) {
        this.inen = inen;
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

        RangeConstraint that = (RangeConstraint) o;

        if (inen != that.inen) return false;
        if (inst != that.inst) return false;
        if (end != null ? !end.equals(that.end) : that.end != null)
            return false;
        if (start != null ? !start.equals(that.start) : that.start != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (inst ? 1 : 0);
        result = 31 * result + (inen ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RangeConstraint{" +
                "start=" + start +
                ", end=" + end +
                ", inst=" + inst +
                ", inen=" + inen +
                '}';
    }
}
