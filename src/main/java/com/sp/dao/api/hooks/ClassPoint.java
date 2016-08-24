package com.sp.dao.api.hooks;

import com.sp.dao.api.IDao;
import com.sp.dao.api.ItemDao;

/**
 * Created by pankajmishra on 24/08/16.
 */
public @interface ClassPoint {
    String target() ;
    String[] methodNames() default {};
}
