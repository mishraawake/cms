package com.sp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.json.ComparableMixin;
import com.sp.service.StringSerialization;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by pankajmishra on 07/08/16.
 */
@Service
public class JsonStringSerializer implements StringSerialization {


    ObjectMapper objectMapper;


    public JsonStringSerializer() {

        objectMapper = new ObjectMapper();
        objectMapper.addMixInAnnotations(Comparable.class, ComparableMixin.class);
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
