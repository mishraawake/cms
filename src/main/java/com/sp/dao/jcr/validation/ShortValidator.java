package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ShortValidator")
public class ShortValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return short.class.equals(suppliedClass) || Short.class.equals(suppliedClass);
    }
}
