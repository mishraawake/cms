package com.sp.model;

/**
 * Created by pankajmishra on 18/08/16.
 */
public class Permission {

    private Identity target;

    private Privilege privilege;

    public Permission(Identity identity, Privilege privilege) {
        this.target = identity;
        this.privilege = privilege;
    }

    public Identity getTarget() {
        return target;
    }

    public void setTarget(Identity target) {
        this.target = target;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "target=" + target +
                ", privilege=" + privilege +
                '}';
    }
}
