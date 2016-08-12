package com.sp.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface StringSerialization {

    <T> T deserialize(String str, Class<T> classParam) throws IOException;

    <T> String serialize(T object) throws JsonProcessingException;
}
