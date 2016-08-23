package com.sp.utils;

import com.sp.dao.api.JCRIRepository;
import org.apache.jackrabbit.util.Text;

/**
 * Created by pankajmishra on 09/08/16.
 */
public class NameUtils {

    /**
     * Trim the string, replace each multi space with one space and then replace every space with hyphen.
     * We need also to ensure that it should not have some character which will create problem in jcr name.
     * So jcr name special character should be filtered out here.
     * TODO - please consider the case when - is already present in the name.
     *
     * @param str
     * @return
     */
    public static String getJCRLikeName(String str) {
        return Text.escapeIllegalJcrChars(str.trim().replaceAll("\\s[\\s]*", "-"));
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getProjectPrefixedName(String name) {
        return getPrefixedName(JCRIRepository.PROJECT_NAME_SPACE_PREFIX, getJCRLikeName(name));
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getUserDefinedPrefixedName(String name) {
        return getPrefixedName(JCRIRepository.USER_DEFINED_NAME_SPACE_PREFIX, getJCRLikeName(name));
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getPackagePrefixedName(String name) {
        return getPrefixedName(JCRIRepository.PACKAGE_NAME_SPACE_PREFIX , getJCRLikeName(name));
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getUserMgmtPrefixedName(String name) {
        return getPrefixedName(JCRIRepository.USER_MGMT_SPACE_PREFIX , getJCRLikeName(name));
    }

    /**
     *
     * @param prefix
     * @param name
     * @return
     */
    private static String getPrefixedName(String prefix, String name) {
        return  prefix.concat(":").concat(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getRolePrefixedName(String name) {
        return getPrefixedName(JCRIRepository.USER_ROLE_SPACE_PREFIX , getJCRLikeName(name));
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getGroupPrefixedName(String name) {
        return getPrefixedName(JCRIRepository.GROUP_ROLE_SPACE_PREFIX , getJCRLikeName(name));
    }
}
