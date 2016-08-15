package com.sp.dao.jcr.model;

import com.sp.model.FieldValue;
import com.sp.model.IUser;
import com.sp.utils.NameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class JCRUser implements IUser, JCRIdentifiable {

    String userName;

    CharSequence passwd;

    List<FieldValue> properties = new ArrayList<FieldValue>();

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public CharSequence getPassword() {
        return passwd;
    }

    @Override
    public void setPassword(CharSequence passwd) {
        this.passwd = passwd;
    }

    @Override
    public List<FieldValue> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(List<FieldValue> properties) {
        this.properties = properties;
    }

    @Override
    public String getIdentityForPath() {
        return NameUtils.getJCRLikeName(userName);
    }
}
