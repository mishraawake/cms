package com.sp.service.impl;

import com.sp.model.IUser;
import com.sp.service.SubjectProvider;

/**
 * Created by pankajmishra on 18/08/16.
 */
public class SimpleSubjectProviderFactory implements SubjectProviderFactory {

    private IUser currentUser;

    public SimpleSubjectProviderFactory(IUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public SubjectProvider getSubjectProvider() {
        return new SubjectProvider() {
            @Override
            public IUser getCurrentSubject() {
                return currentUser;
            }
        };
    }
}
