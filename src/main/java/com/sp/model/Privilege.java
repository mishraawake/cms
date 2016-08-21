package com.sp.model;

/**
 * Created by pankajmishra on 17/08/16.
 */
public enum Privilege {

    /**
     * All permission for reading as well as writing to the folder and below folder. But here we need to
     * define semantic for the dynamic packaging
     */
    Executive {
        public Privilege[] implied() {
            return Privilege.values();
        }
    },

    /**
     * Write permission on the folder, includes modification of properties, its child in recursively.
     */
    Write {
        public Privilege[] implied() {
            Privilege[] privileges = {AddChild, RemoveChild, ModifyProperties, RemoveNode};
            return privileges;
        }
    },

    /**
     * Only add child permission exists on the target node.
     */
    AddChild {
        public Privilege[] implied() {
            Privilege[] privileges = {};
            return privileges;
        }
    },

    /**
     * Only remove
     */
    RemoveChild {
        public Privilege[] implied() {
            Privilege[] privileges = {};
            return privileges;
        }
    },

    /**
     *
     */
    RemoveNode {
        public Privilege[] implied() {
            Privilege[] privileges = {RemoveChild};
            return privileges;
        }
    },

    /**
     * Read permission of folder  and recursively of its child.
     */
    Read {
        public Privilege[] implied() {
            Privilege[] privileges = {};
            return privileges;
        }
    },

    /**
     *
     */
    ModifyProperties {
        public Privilege[] implied() {
            Privilege[] privileges = {};
            return privileges;
        }
    },

    /**
     * Adding user and deleting user, adding group and deleting group.
     */
    UserAndGroupAdd {
        public Privilege[] implied() {
            Privilege[] privileges = {};
            return privileges;
        }
    };

    public abstract Privilege[] implied();

}
