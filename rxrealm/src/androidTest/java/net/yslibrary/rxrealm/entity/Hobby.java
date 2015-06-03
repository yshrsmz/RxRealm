package net.yslibrary.rxrealm.entity;

import io.realm.RealmObject;

/**
 * Created by shimizu_yasuhiro on 15/06/03.
 */
public class Hobby extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
