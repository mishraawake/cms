package com.sp.service.impl;

import com.sp.model.IUser;
import com.sp.service.RunTimeContext;
import com.sp.service.SubjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 18/08/16.
 */
@Service
public class SimpleSubjectProvider implements SubjectProvider {


    public static final String CURRENT_LOGGEDIN_USER = "current_user";


    @Autowired
    RunTimeContext<IUser> iUserRunTimeContext;


    @Override
    public IUser getCurrentSubject() {
        return iUserRunTimeContext.getValue(CURRENT_LOGGEDIN_USER);
    }
}
