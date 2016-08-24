package com.sp.dao.jcr.validation;

import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfByteValidator")
public class ArrayOfByteValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return byte[].class.equals(suppliedClass) || Byte[].class.equals(suppliedClass);
    }
}
