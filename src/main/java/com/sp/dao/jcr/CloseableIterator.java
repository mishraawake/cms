package com.sp.dao.jcr;

import java.util.Iterator;

/**
 * Created by pankajmishra on 22/08/16.
 */
public interface CloseableIterator<T> extends Iterator<T> , AutoCloseable {
    /**
     * This type of iterator need some system resource. Please close once it is used, otherwise you are risking of
     * memory leaks.
     */
    @Override
    void close();
}
