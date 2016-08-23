package com.sp.model;

import java.io.Serializable;

/**
 * Created by pankajmishra on 18/08/16.
 */

public final class Permission {

    private Serializable targetId;

    private Privilege privilege;

    private boolean allowed;

    public Permission(){

    }

    public Permission(Serializable targetId, Privilege privilege, boolean allowed) {
        this.targetId = targetId;
        this.privilege = privilege;
        this.allowed = allowed;
    }

    public Serializable getTargetId() {
        return targetId;
    }


    public Privilege getPrivilege() {
        return privilege;
    }

    public boolean getAllowed(){
        return allowed;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "target=" + targetId +
                ", privilege=" + privilege +
                '}';
    }
}
