package com.sp.dao.jcr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.model.Field;
import com.sp.model.IDefinition;
import com.sp.model.IUser;
import com.sp.utils.NameUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 *
 * Definition object is used to define characteristics of an entity.
 *
 * For example suppose we have following item present in news cms. Article, Photo, Video
 * Now someone might want to create a new type <i>poll</i>. The structure of poll could be different than
 * any encountered type and we might have to create a new definition object called poll for that.
 * This definition object will be used to verify poll object that will be generated in cms and
 * also it will be used to generate form for the poll.
 */
public class JCRDefinition implements JCRIdentifiable, IDefinition<JCRDefinition> {

    private String __id;
    private String name;
    private String description;
    private List<Field> fields = new ArrayList<Field>();
    private JCRDefinition parentDefinition;
    private Date createdDate;
    private IUser createdBy;

    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
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

    public JCRDefinition getParentDefinition() {
        return parentDefinition;
    }

    public void setParentDefinition(JCRDefinition parentDefinition) {
        this.parentDefinition = parentDefinition;
    }

    public static JCRDefinition getDefinitionFromString(String json){
        return null;
    }

    @JsonIgnore
    @Override
    public String getIdentityForPath() {
        return NameUtils.getSEOLikeString(name);
    }

    @Override
    public Date getCreateDate() {
        return createdDate;
    }

    @Override
    public void setCreateDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public IUser getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(IUser createdBy) {
        this.createdBy = createdBy;
    }

}