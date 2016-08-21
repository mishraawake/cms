package com.sp.model;

import java.io.Serializable;

/**
 * Created by pankajmishra on 09/08/16.
 * <p>
 * One should work with id assuming that it is serializable, otherwise later code might break
 * because some other dao may implement id as other type.
 */
public interface Identity {
    public Serializable get__id();
}
