package com.sp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankajmishra on 17/08/16.
 * A collection of permissions. Here permission is combination of entities and action on that entites.
 * So a role can be translated into number of items and permitted actions on those items.
 *
 */
public class Role {



    List<Permission> permissions = new ArrayList<>();
    String name;


    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
