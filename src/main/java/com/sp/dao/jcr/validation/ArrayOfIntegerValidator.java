package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfIntegerValidator")
public class ArrayOfIntegerValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return int[].class.equals(suppliedClass) || Integer[].class.equals(suppliedClass);
    }
}
