package com.sp.utils;

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
}
