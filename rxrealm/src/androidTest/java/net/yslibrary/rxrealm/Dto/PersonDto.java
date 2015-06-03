package net.yslibrary.rxrealm.Dto;

import java.util.List;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public class PersonDto {

    private String mFirstName;

    private String mLastName;

    private List<HobbyDto> mHobbies;

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public List<HobbyDto> getHobbies() {
        return mHobbies;
    }

    public PersonDto(String firstName, String lastName, List<HobbyDto> hobbies) {
        mFirstName = firstName;
        mLastName = lastName;
        mHobbies = hobbies;
    }

}
