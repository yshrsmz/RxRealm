package net.yslibrary.rxrealm;

import net.yslibrary.rxrealm.Dto.PersonDto;
import net.yslibrary.rxrealm.entity.Person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import io.realm.Realm;
import rx.functions.Func1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
@RunWith(AndroidJUnit4.class)
public class RealmObservableTest {

    private static final String REALM_NAME = "rxrealm.realm";

    private Realm mRealm;

    @Before
    public void createRealm() {
        Context context = InstrumentationRegistry.getTargetContext();
        Realm.deleteRealmFile(context, REALM_NAME);
        Realm.getInstance(context, REALM_NAME);
    }

    @Test
    public void RealmObservable_object() {

        Context context = InstrumentationRegistry.getTargetContext();

        PersonDto person = RealmObservable.object(context, REALM_NAME, new Func1<Realm, Person>() {
            @Override
            public Person call(Realm realm) {
                Person person = realm.createObject(Person.class);
                person.setFirstName("John");
                person.setLastName("Smith");

                return person;
            }
        }).map(new Func1<Person, PersonDto>() {
            @Override
            public PersonDto call(Person person) {
                return new PersonDto(person.getFirstName(), person.getLastName());
            }
        }).toBlocking().first();

        assertThat(person.getFirstName(), is("John"));
        assertThat(person.getLastName(), is("Smith"));
    }

}
