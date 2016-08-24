package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfDateValidator")
public class ArrayOfDateValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return Date[].class.equals(suppliedClass);
    }
}
