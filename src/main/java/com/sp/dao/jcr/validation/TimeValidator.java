package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("TimeValidator")
public class TimeValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return double.class.equals(suppliedClass) || Double.class.equals(suppliedClass);
    }
}
