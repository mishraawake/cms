package com.sp.dao.jcr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 * Individual item that will be present in cms. It must have a definition object. An item can have
 * many kind of associations.
 */
public class JCRItem implements JCRIdentifiable, IItem<JCRItem, JCRDefinition> {

    private JCRItem parentItem;
    private Serializable __id;
    private String name;
    private JCRDefinition definition;
    private List<FieldValue> fieldValues = new ArrayList<FieldValue>();
    private List<Association<JCRItem>> associations = new ArrayList<Association<JCRItem>>();
    List<JCRItem> children = new ArrayList<>();
    private Date createdDate;
    private IUser createdBy;

    public Serializable get__id() {
        return __id;
    }

    public void set__id(Serializable __id){
        this.__id = __id;
    }
    @Override
    public JCRItem getParentItem() {
        return parentItem;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setParentItem(JCRItem item) {
        parentItem = item;
    }

    public JCRDefinition getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(JCRDefinition definition) {
        this.definition = definition;
    }


    public List<FieldValue> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<FieldValue> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public List<Association<JCRItem>> getAssociations() {
        return associations;
    }

    @Override
    public List<JCRItem> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<JCRItem> children) {
        this.children = children;
    }

    public void setAssociations(List<Association<JCRItem>> associations) {
        this.associations = associations;
    }

    public FieldValue getFieldValue(Field field) {
        for (FieldValue fieldValue : fieldValues) {
            if (fieldValue.getField().equals(field)) {
                return fieldValue;
            }
        }
        return null;
    }

    @JsonIgnore
    @Override
    public String getIdentityForPath() {
        return name;
    }

    @Override
    public Date getCreateDate() {
        // defensive copying
        if (createdDate == null) {
            return null;
        }
        return (Date) createdDate.clone();
    }

    @Override
    public void setCreateDate(Date createdDate) {
        if (createdDate == null) {
            return;
        }
        this.createdDate = (Date) createdDate.clone();
    }

    @Override
    public IUser getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(IUser createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "JCRItem{" +
                ", __id='" + __id + '\'' +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy=" + createdBy +
                ",  definition=" + definition +
                //  ", \n fieldValues=" + fieldValues +
                ", \n associations=" + associations +
                ", \n children=" + children +
                '}';
    }
}
