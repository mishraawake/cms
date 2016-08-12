package com.sp.dao.jcr.model;

import com.sp.model.IUser;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class JCRUser implements IUser, JCRIdentifiable {


    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String passwd) {

    }

    @Override
    public String getIdentityForPath() {
        return null;
    }
}
