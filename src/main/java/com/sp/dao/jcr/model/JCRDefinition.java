package com.sp.dao.jcr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.model.Field;
import com.sp.model.IDefinition;
import com.sp.model.IUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 * <p>
 * Definition object is used to define characteristics of an entity.
 * <p>
 * For example suppose we have following items present in news cms. Article, Photo, Video
 * Now someone might want to create a new type <i>poll</i>. The structure of poll could be different than
 * any encountered type and we might have to create a new definition object called poll for that.
 * This definition object will be used to verify poll object that will be generated in cms and
 * also it will be used to generate form for the poll.
 */
public class JCRDefinition implements JCRIdentifiable, IDefinition {

    private Serializable __id;
    private String name;
    private String description;
    private List<Field> fields = new ArrayList<Field>();
    private Date createdDate;
    private IUser createdBy;

    public Serializable get__id() {
        return __id;
    }

    public void set__id(Serializable __id) {
        this.__id = __id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public Field getFieldByName(String name) {
        for(Field field : fields){
            if(field.getName().equals(name)){
                return field;
            }
        }
        return null;
    }

    public static JCRDefinition getDefinitionFromString(String json) {
        return null;
    }

    @JsonIgnore
    @Override
    public String getIdentityForPath() {
        return name;
    }

    @Override
    public Date getCreateDate() {
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
        return "JCRDefinition{" +
                "__id='" + __id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                ", createdDate=" + createdDate +
                ", createdBy=" + createdBy +
                '}';
    }
}
