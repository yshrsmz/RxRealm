package net.yslibrary.rxrealm.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public class Person extends RealmObject {

    @PrimaryKey
    private String firstName;

    private String lastName;

    private RealmList<Hobby> hobbies;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RealmList<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(RealmList<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
}
