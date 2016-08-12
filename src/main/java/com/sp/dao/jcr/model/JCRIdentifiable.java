package com.sp.dao.jcr.model;

import com.sp.model.Identifiable;

/**
 * Created by pankajmishra on 09/08/16.
 */
public interface JCRIdentifiable extends Identifiable {
    String getIdentityForPath();
}
