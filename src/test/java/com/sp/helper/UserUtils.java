package com.sp.helper;

import com.sp.model.IUser;
import com.sp.model.PojoFactory;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by pankajmishra on 15/08/16.
 */
public class UserUtils {

    public static IUser getDummyUser(PojoFactory pojoFactory){
        IUser iUser = pojoFactory.getNewUser();
        iUser.setUserName( RandomStringUtils.randomAlphanumeric(20));
        iUser.setPassword(RandomStringUtils.randomAlphanumeric(20));
        iUser.setProperties(ItemUtils.getDummyFieldValueList(1));
        return iUser;
    }
}
