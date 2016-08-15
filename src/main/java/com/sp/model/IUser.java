package com.sp.model;

import java.util.List;

/**
 * Created by pankajmishra on 09/08/16.
 */
public interface IUser {

    String getUserName();

    void setUserName(String userName);

    CharSequence getPassword();

    void setPassword(CharSequence passwd);

    List<FieldValue> getProperties();

    void setProperties(List<FieldValue> fieldValue);
}
