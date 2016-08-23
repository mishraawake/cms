package com.sp.dao.jcr.model;

import com.sp.dao.api.JCRIRepository;
import com.sp.utils.NameUtils;

/**
 * Created by pankajmishra on 21/08/16.
 */
public class JcrNameFac {
    public static JcrName getUserName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getUserDefinedPrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.USER_DEFINED_NAME_SPACE_PREFIX + ":" + name;
            }

        };
    }

    public static JcrName getProjectName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getProjectPrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.PROJECT_NAME_SPACE_PREFIX + ":" + name;
            }


        };
    }

    public static JcrName getPackageName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getPackagePrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.PACKAGE_NAME_SPACE_PREFIX + ":" + name;
            }

        };
    }

    public static JcrName getUserMgmtName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getUserMgmtPrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.USER_MGMT_SPACE_PREFIX + ":" + name;
            }

        };
    }

    public static JcrName getRoleName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getRolePrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.USER_ROLE_SPACE_PREFIX + ":" + name;
            }

        };
    }

    public static JcrName getGroupName(String name){
        return new JcrName() {
            @Override
            public String name() {
                return NameUtils.getGroupPrefixedName(name);
            }

            @Override
            public String pattern() {
                return JCRIRepository.GROUP_ROLE_SPACE_PREFIX + ":" + name;
            }

        };
    }
}
