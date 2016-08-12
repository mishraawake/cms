package com.sp.utils;

/**
 * Created by pankajmishra on 09/08/16.
 */
public class NameUtils {

    // trim the string, replace each multi space with one space and then replace every space with hyphen.
    public static String getSEOLikeString(String str){
        return str.trim().replaceAll("\\s[\\s]*", "-");
    }
}
