package com.sp.model;

import com.sp.validate.constraint.Constraint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class Field {

    private String name;
    private ValueType valueType;
    private boolean searchable;
    private List<Constraint> constraints = new ArrayList<Constraint>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (searchable != field.searchable) return false;
        if (constraints != null ? !constraints.equals(field.constraints) : field.constraints != null)
            return false;
        if (name != null ? !name.equals(field.name) : field.name != null)
            return false;
        if (valueType != field.valueType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        result = 31 * result + (searchable ? 1 : 0);
        result = 31 * result + (constraints != null ? constraints.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", valueType=" + valueType +
                ", searchable=" + searchable +
                ", constraints=" + constraints +
                '}';
    }
}
