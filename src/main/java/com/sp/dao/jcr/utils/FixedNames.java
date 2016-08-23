package com.sp.dao.jcr.utils;

import com.sp.dao.jcr.model.JcrName;
import com.sp.dao.jcr.model.JcrNameFac;

/**
 * Created by pankajmishra on 13/08/16.
 */
public class FixedNames {

    // ITs a utility class.
    private FixedNames(){

    }

    private static final java.lang.String FIELDS_LINK_NAME = "fields";
    private static final java.lang.String FIELD_LINK_NAME = "field";
    private static final java.lang.String DEF_LINK_NAME = "def";
    private static final java.lang.String DEF_ARRAY_IDENTITY_NAME = "defa";
    private static final java.lang.String ITEM_LINK_NAME = "item";
    private static final java.lang.String NAME_LINK_NAME = "name";
    private static final java.lang.String DESC_LINK_NAME = "desc";
    private static final java.lang.String BINARIES_LINK_NAME = "binaries";
    private static final java.lang.String VALUE_LINK_NAME = "value";
    private static final java.lang.String CHILD_LINK_VALUE = "child";
    private static final java.lang.String USER_LINK_VALUE = "user";
    private static final java.lang.String CREATED_AT_LINK_VALUE = "createdAt";
    private static final java.lang.String CREATED_BY_LINK_VALUE = "createdBy";
    private static final java.lang.String ROLE_LINK_NAME = "role";


    public static JcrName fields(){
        return JcrNameFac.getProjectName(FIELDS_LINK_NAME);
    }

    public static JcrName field(){
        return JcrNameFac.getProjectName(FIELD_LINK_NAME);
    }

    public static JcrName def(){
        return JcrNameFac.getProjectName(DEF_LINK_NAME);
    }

    public static JcrName defSize(){
        return JcrNameFac.getProjectName(DEF_ARRAY_IDENTITY_NAME);
    }

    public static JcrName item(){
        return JcrNameFac.getProjectName(ITEM_LINK_NAME);
    }

    public static JcrName name(){
        return JcrNameFac.getProjectName(NAME_LINK_NAME);
    }

    public static JcrName desc(){
        return JcrNameFac.getProjectName(DESC_LINK_NAME);
    }

    public static JcrName binaries(){
        return JcrNameFac.getProjectName(BINARIES_LINK_NAME);
    }

    public static JcrName value(){
        return JcrNameFac.getProjectName(VALUE_LINK_NAME);
    }

    public static JcrName value(int index){
        return JcrNameFac.getProjectName(VALUE_LINK_NAME + "-" + index);
    }

    public static JcrName child(){
        return JcrNameFac.getProjectName(CHILD_LINK_VALUE);
    }

    public static JcrName user(){
        return JcrNameFac.getProjectName(USER_LINK_VALUE);
    }

    public static JcrName createAt(){
        return JcrNameFac.getProjectName(CREATED_AT_LINK_VALUE);
    }

    public static JcrName createdBy(){
        return JcrNameFac.getProjectName(CREATED_BY_LINK_VALUE);
    }

    public static JcrName role(){
        return JcrNameFac.getUserMgmtName(ROLE_LINK_NAME);
    }

}
