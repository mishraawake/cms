package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("CharValidator")
public class CharValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return char.class.equals(suppliedClass) || Character.class.equals(suppliedClass);
    }
}
