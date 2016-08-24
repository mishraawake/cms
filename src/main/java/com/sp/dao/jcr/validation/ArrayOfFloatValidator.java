package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfFloatValidator")
public class ArrayOfFloatValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return float[].class.equals(suppliedClass) || Float[].class.equals(suppliedClass);
    }
}
