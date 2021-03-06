package com.sp.model;

import java.util.List;

/**
 * Created by pankajmishra on 09/08/16.
 * <p>
 * It defines the type of data that will be created.
 */
public interface IDefinition extends Auditable, Identity {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public List<Field> getFields();

    public void setFields(List<Field> fields);

    public Field getFieldByName(String name);
}
