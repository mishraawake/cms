package com.sp.service.impl;

import com.sp.model.FieldValue;
import com.sp.model.IUser;
import com.sp.model.PojoFactory;
import com.sp.service.RunTimeContext;
import com.sp.service.SubjectProvider;
import org.apache.jackrabbit.oak.spi.security.user.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by pankajmishra on 18/08/16.
 */
@Service
public class SimpleSubjectProvider implements SubjectProvider {


    public static final String CURRENT_LOGGEDIN_USER = "current_user";

    @Autowired
    PojoFactory pojoFactory;

    @PostConstruct
    private void initializeWithAdmin(){
        IUser adminUser = pojoFactory.getNewUser();
        adminUser.setUserName("admin");
        adminUser.setPassword("admin".toCharArray());
        iUserRunTimeContext.setValue(CURRENT_LOGGEDIN_USER, adminUser);
    }

    @Autowired
    RunTimeContext<IUser> iUserRunTimeContext;


    @Override
    public IUser getCurrentSubject() {
        return iUserRunTimeContext.getValue(CURRENT_LOGGEDIN_USER);
    }
}
