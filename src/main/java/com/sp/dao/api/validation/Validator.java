package com.sp.dao.api.validation;

import com.sp.model.Field;

import java.util.Map;

/**
 * Created by pankajmishra on 24/08/16.
 */
public interface Validator<T> {
    boolean validate(T candidate, Map<Field, String> errorMessage);
}
