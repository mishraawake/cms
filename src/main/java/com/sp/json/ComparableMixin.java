package com.sp.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

/**
 * Created by pankajmishra on 07/08/16.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Integer.class, name = "int"),
        @JsonSubTypes.Type(value = Long.class, name = "long"),
        @JsonSubTypes.Type(value = Date.class, name = "date"),
        @JsonSubTypes.Type(value = Float.class, name = "float"),
        @JsonSubTypes.Type(value = Double.class, name = "double")
})
public class ComparableMixin {
}
