package com.sp.dao.jcr.model;

/**
 * Created by pankajmishra on 21/08/16.
 *
 *
 */
public interface JcrName {

    /**
     * Implementation will normalize give string into jcr name.
     * @return
     */
    String name();

    /**
     * Implementation will not normalize give string into jcr name.
     * @return
     */
    String pattern();
}
