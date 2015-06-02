package net.yslibrary.rxrealm.Dto;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public class PersonDto {

    private String mFirstName;

    private String mLastName;

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public PersonDto(String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;

    }

}
