package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("StringValidator")
public class StringValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return String.class.equals(suppliedClass);
    }
}
