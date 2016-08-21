package com.sp.service;

import com.sp.model.Identifiable;

/**
 * Created by pankajmishra on 09/08/16.
 * An id generator for the cms.
 */
public interface IdGenerator<T, S extends Identifiable> {
    T getNextId(S ephemeral);
}
