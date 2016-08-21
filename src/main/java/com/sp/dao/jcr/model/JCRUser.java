package com.sp.dao.jcr.model;

import com.sp.model.FieldValue;
import com.sp.model.IUser;
import com.sp.utils.NameUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class JCRUser implements IUser, JCRIdentifiable {

    String userName;

    char[] passwd;

    List<FieldValue> properties = new ArrayList<FieldValue>();

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public char[] getPassword() {
        if (passwd == null) {
            return null;
        }
        return Arrays.copyOf(passwd, passwd.length);
    }

    @Override
    public void setPassword(char[] passwd) {
        if (passwd == null) {
            return;
        }
        this.passwd = Arrays.copyOf(passwd, passwd.length);
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

    @Override
    public String toString() {
        return "JCRUser{" +
                "userName='" + userName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
