package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfStringValidator")
public class ArrayOfStringValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return String[].class.equals(suppliedClass);
    }
}
