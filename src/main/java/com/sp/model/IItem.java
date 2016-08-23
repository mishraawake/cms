package com.sp.model;

import java.util.List;

/**
 * Created by pankajmishra on 09/08/16.
 */
public interface IItem<T extends IItem, D extends IDefinition> extends Auditable, Identity {

    public T getParentItem();

    public void setParentItem(T parent);

    public String getName();

    public void setName(String name);

    public D getDefinition();

    public void setDefinition(D definition);

    public List<FieldValue> getFieldValues();

    public void setFieldValues(List<FieldValue> fields);

    public List<Association<T>> getAssociations();

    public List<T> getChildren();

    public void setChildren(List<T> children);

    public void setAssociations(List<Association<T>> associations);

    public <F> FieldValue<F> getFieldValue(Field field);

}
