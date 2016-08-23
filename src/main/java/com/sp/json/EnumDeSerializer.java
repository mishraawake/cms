package com.sp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.sp.model.Privilege;

import java.io.IOException;

/**
 * Created by pankajmishra on 22/08/16.
 */
public class EnumDeSerializer extends JsonDeserializer<Privilege> {

    @Override
    public Privilege deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return parseEnum(jp, ctxt);
    }

    @Override
    public Privilege deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                                      TypeDeserializer typeDeserializer)
            throws IOException, JsonProcessingException
    {
        // We could try calling
        return deserialize(jp, ctxt);
    }

    private Privilege parseEnum(JsonParser jp, DeserializationContext ctxt)  throws IOException{
        JsonToken t = jp.getCurrentToken();
        // And finally, let's allow Strings to be converted too
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            return Enum.valueOf(Privilege.class, text);
        }
        return null;
    }
}
