package com.sp.dao.jcr.validation;

import com.sp.model.FileObject;
import com.sp.model.Image;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("FileValidator")
public class FileValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return FileObject.class.equals(suppliedClass);
    }
}
