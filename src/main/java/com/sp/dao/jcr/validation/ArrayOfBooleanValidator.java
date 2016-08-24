package com.sp.dao.jcr.validation;

import com.sp.dao.api.exchange.SpToJcr;
import com.sp.dao.api.validation.Validator;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.validate.constraint.Constraint;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Map;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ArrayOfBooleanValidator")
public class ArrayOfBooleanValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return boolean[].class.equals(suppliedClass) || Boolean[].class.equals(suppliedClass);
    }
}
