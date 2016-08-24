package com.sp.dao.api.hooks;

/**
 * Created by pankajmishra on 24/08/16.
 */
public @interface CallPoint {
    ClassPoint[] classNames() default {};
}
