package com.sp.service.impl;

import com.sp.model.IUser;
import com.sp.service.SubjectProvider;

/**
 * Created by pankajmishra on 18/08/16.
 */
public interface SubjectProviderFactory {

    public abstract SubjectProvider getSubjectProvider();
}
