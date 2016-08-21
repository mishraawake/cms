package com.sp.model;

import java.util.Date;

/**
 * Created by pankajmishra on 06/08/16.
 */
public interface Auditable {
    Date getCreateDate();

    void setCreateDate(Date createdAt);

    IUser getCreatedBy();

    void setCreatedBy(IUser user);
}
