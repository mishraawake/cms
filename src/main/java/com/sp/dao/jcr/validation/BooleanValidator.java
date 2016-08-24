package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("BooleanValidator")
public class BooleanValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return boolean.class.equals(suppliedClass) || Boolean.class.equals(suppliedClass);
    }
}
