package com.sp.dao.jcr.validation;

import com.sp.model.Video;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 23/08/16.
 */
@Service("VideoValidator")
public class VideoValidator extends AbstractValidator {
    @Override
    public boolean support(Class<?> suppliedClass) {
        return Video.class.equals(suppliedClass);
    }
}
