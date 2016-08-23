package com.sp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.validate.constraint.Constraint;

import java.util.List;

/**
 * Created by pankajmishra on 21/08/16.
 * A field who is definition in itself. So in object oriented terminology it is a class as a field inside
 * a class.
 */
public class DefinitionField extends Field {

    private IDefinition definition;

    @JsonIgnore
    public IDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(IDefinition definition) {
        this.definition = definition;
    }

    public ValueType getValueType() {
        return super.getValueType();
    }

    @Override
    public void setValueType(ValueType valueType) {
        if(valueType.isDefinition()){
            super.setValueType(valueType);
            return;
        }
        throw new IllegalArgumentException(String.format("Only allowed value type are %s, %s but... trying to set %s.",
                ValueType.Definition, ValueType.ArrayOfDefinition, valueType));
    }

    @JsonIgnore
    public List<Constraint> getConstraints() {
        throw new IllegalAccessError("Not allowed. These are defined inside this definition fields");
    }


    public void setConstraints(List<Constraint> constraints) {
        throw new IllegalAccessError("Not allowed. There are defined inside this definition fields");
    }
}
