package com.sp.model;

import com.sp.dao.jcr.model.JCRItem;

import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class AssociationLink<T extends IItem> {

    private T linked;
    List<FieldValue> properties;

    public List<FieldValue> getProperties() {
        return properties;
    }

    public void setProperties(List<FieldValue> properties) {
        this.properties = properties;
    }

    public T getLinked() {
        return linked;
    }

    public void setLinked(T linked) {
        this.linked = linked;
    }

    @Override
    public String toString() {
        return "AssociationLink{" +
                "linked=" + linked +
                ", properties=" + properties +
                '}';
    }
}
