package com.sp.model;

import com.sp.dao.jcr.model.JCRItem;

import java.util.Date;

/**
 * Created by pankajmishra on 06/08/16.
 *
 * An association object is owned by some entity which is represented in me field. It will have a number
 * of associates item.
 */
public class Association implements Auditable {

    private String name;
    private String description;
    private IItem me;
    private Iterable<AssociationLink> associates;

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public void setCreateDate(Date createdAt) {

    }

    @Override
    public IUser getCreatedBy() {
        return null;
    }

    @Override
    public void setCreatedBy(IUser user) {

    }
}