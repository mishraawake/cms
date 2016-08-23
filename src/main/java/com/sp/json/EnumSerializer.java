package com.sp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * Created by pankajmishra on 22/08/16.
 */
public class EnumSerializer extends JsonSerializer<Enum> {

    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString (value.name());
    }

    @Override
    public void serializeWithType(Enum value, JsonGenerator gen,
                                  SerializerProvider provider, TypeSerializer typeSer)
            throws IOException, JsonProcessingException {

        serialize(value, gen, provider); // call your customized serialize method
    }
}
