package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by pankajmishra on 06/08/16.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RangeConstraint.class, name = "RangeConstraint"),
        @JsonSubTypes.Type(value = LengthConstraint.class, name = "LengthConstraint"),
        @JsonSubTypes.Type(value = LessThanContraint.class, name = "LessThanContraint"),
        @JsonSubTypes.Type(value = GreaterThanConstraint.class, name = "GreaterThanConstraint"),
})
public interface Constraint<T> {
    boolean pass(T value);
}
