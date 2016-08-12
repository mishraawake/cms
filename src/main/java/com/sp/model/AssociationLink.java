package com.sp.model;

import com.sp.dao.jcr.model.JCRItem;

import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class AssociationLink {
    private String name;
    private JCRItem linked;
    List<FieldValue> properties;
}
