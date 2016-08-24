package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("LongValidator")
public class LongValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return long.class.equals(suppliedClass) || Long.class.equals(suppliedClass);
    }
}
