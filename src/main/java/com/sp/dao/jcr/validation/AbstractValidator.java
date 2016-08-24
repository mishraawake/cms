package com.sp.dao.jcr.validation;

import com.sp.dao.api.validation.Validator;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import com.sp.validate.constraint.Constraint;

import java.util.Map;

/**
 * Created by pankajmishra on 24/08/16.
 */
public abstract class AbstractValidator implements Validator<FieldValue> {

    private final String  MANDATORY = "Field is mandotory but supplied as null";
    private final String CLASS_TYPE_WARNING = "supposed to be %s but supplied as %s";

    @Override
    public boolean validate(FieldValue candidate, Map<Field, String> errorMap) {
        try {
            if (candidate == null) {
                return false;
            }

            if (candidate.getValue() == null) {
                if (candidate.getField().isMandatory()) {
                    errorMap.put(candidate.getField(), MANDATORY);
                    return false;
                }
                return true;
            }


            boolean result = false;
            if (support(candidate.getValue().getClass())) {
                result = true;
                for (Constraint constraint : candidate.getField().getConstraints()) {
                    if (!constraint.pass(candidate.getValue())) {
                        errorMap.put(candidate.getField(), constraint.getErrorMessage());
                        result = false;
                    }
                }
                return result = result & true;
            } else {
                //TODO : move this to respective validator class.
                //errorMap.put(candidate.getField(), String.format(CLASS_TYPE_WARNING, boolean.class.getName(),
                  //      candidate.getValue().getClass()));

            }
            return result;
        } catch (Exception e){
            throw new RuntimeException("Unknow exception in validation", e);
        }
    }

    protected abstract boolean support(Class<?> suppliedClass);
}
