package com.sp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sp.json.ComparableMixin;
import com.sp.json.SerializableMixin;
import com.sp.service.StringSerialization;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by pankajmishra on 07/08/16.
 */
@Service
public class JsonStringSerializer implements StringSerialization {


    ObjectMapper objectMapper;


    public JsonStringSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Comparable.class, ComparableMixin.class);
        objectMapper.addMixIn(Serializable.class, SerializableMixin.class);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }


    @Override
    public <T> T deserialize(String str, Class<T> classPa) throws IOException {
        return objectMapper.readValue(str, classPa);
    }

    @Override
    public <T> String serialize(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

}
