package com.sp.model;

import com.sp.dao.jcr.model.JCRIdentifiable;
import com.sp.dao.jcr.model.JCRItem;
import com.sp.utils.NameUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 *
 * An association object is owned by some entity which is represented in me field. It will have a number
 * of associated items.
 */
public class Association<T extends IItem> implements Auditable, JCRIdentifiable {

    private String name;
    private IItem me;
    private List<AssociationLink<T>> associates = new ArrayList<AssociationLink<T>>();
    private Date createdDate;
    private IUser createdBy;
    List<FieldValue> properties;

    @Override
    public Date getCreateDate() {
        return (Date)createdDate.clone();
    }

    @Override
    public void setCreateDate(Date createdDate) {
        this.createdDate = (Date)createdDate.clone();
    }

    @Override
    public IUser getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(IUser createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IItem getMe() {
        return me;
    }

    public void setMe(IItem me) {
        this.me = me;
    }

    public List<AssociationLink<T>> getAssociates() {
        return associates;
    }

    public void setAssociates(List<AssociationLink<T>> associates) {
        this.associates = associates;
    }

    public List<FieldValue> getProperties() {
        return properties;
    }

    public void setProperties(List<FieldValue> properties) {
        this.properties = properties;
    }

    @Override
    public String getIdentityForPath() {
        return NameUtils.getJCRSEOLikeString(name);
    }

    @Override
    public String toString() {
        return "Association{" +
                "name='" + name + '\'' +
                ", me=" + me +
                ", associates=" + associates +
                ", createdDate=" + createdDate +
                ", createdBy=" + createdBy +
                ", properties=" + properties +
                '}';
    }
}