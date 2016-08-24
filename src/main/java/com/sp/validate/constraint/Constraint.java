package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
        @JsonSubTypes.Type(value = LessThanConstraint.class, name = "LessThanConstraint"),
        @JsonSubTypes.Type(value = GreaterThanConstraint.class, name = "GreaterThanConstraint"),
        @JsonSubTypes.Type(value = ValueConstraint.class, name = "ValueConstraint"),
})
public interface Constraint<T> {

        /**
         * Whether this constraint passed or failed.
         * @param value
         * @return
         */
    boolean pass(T value);

        /**
         * What is error message in case it failed.
         * @return
         */
        String getErrorMessage();
}
