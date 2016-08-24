package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfShortValidator")
public class ArrayOfShortValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return short[].class.equals(suppliedClass) || Short[].class.equals(suppliedClass);
    }
}
