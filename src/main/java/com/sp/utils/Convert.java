package com.sp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 13/08/16.
 */
public class Convert {

    public static <T> List<T> fromArrayToList(T[] array) {
        List<T> lists = new ArrayList<T>();

        for (T object : array) {
            lists.add(object);
        }
        return lists;
    }


}
