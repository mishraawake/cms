package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfDoubleValidator")
public class ArrayOfDoubleValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return double[].class.equals(suppliedClass) || Double[].class.equals(suppliedClass);
    }
}
