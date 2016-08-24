package com.sp.dao.jcr.validation;

import com.sp.model.FieldValue;
import com.sp.model.FileObject;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("DefinitionValidator")
public class DefinitionValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return FieldValue[].class.equals(suppliedClass);
    }
}
