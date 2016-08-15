package com.sp.model;

/**
 * Created by pankajmishra on 10/08/16.
 */
public interface PojoFactory {
    IDefinition getNewDefinition();
    IItem getNewItem();
    IUser getNewUser();

}
