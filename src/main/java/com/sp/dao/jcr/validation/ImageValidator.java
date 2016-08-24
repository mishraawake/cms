package com.sp.dao.jcr.validation;

import com.sp.model.Image;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("ImageValidator")
public class ImageValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return Image.class.equals(suppliedClass);
    }
}
