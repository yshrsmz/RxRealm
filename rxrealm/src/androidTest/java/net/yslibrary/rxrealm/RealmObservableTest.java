package net.yslibrary.rxrealm;

import net.yslibrary.rxrealm.Dto.HobbyDto;
import net.yslibrary.rxrealm.Dto.PersonDto;
import net.yslibrary.rxrealm.entity.Hobby;
import net.yslibrary.rxrealm.entity.Person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

import static org.assertj.core.api.Assertions.assertThat;


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

                RealmList<Hobby> hobbies = new RealmList<Hobby>();
                Hobby hobby = realm.createObject(Hobby.class);
                hobby.setName("tv game");

                hobbies.add(hobby);

                person.setHobbies(hobbies);

                return person;
            }
        }).map(new Func1<Person, PersonDto>() {
            @Override
            public PersonDto call(Person person) {
                List<HobbyDto> hobbies = Observable.from(person.getHobbies())
                        .map(new Func1<Hobby, HobbyDto>() {
                            @Override
                            public HobbyDto call(Hobby hobby) {
                                return new HobbyDto(hobby.getName());
                            }
                        }).toList().toBlocking().single();

                return new PersonDto(person.getFirstName(), person.getLastName(), hobbies);
            }
        }).toBlocking().first();

        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("Smith");
        assertThat(person.getHobbies()).hasSize(1);
        assertThat(person.getHobbies().get(0).getName()).isEqualTo("tv game");
    }

    @Test
    public void RealmObservable_list() {

        Context context = InstrumentationRegistry.getTargetContext();

        // setup
        RealmObservable.object(context, REALM_NAME, new Func1<Realm, Person>() {
            @Override
            public Person call(Realm realm) {
                Person person = realm.createObject(Person.class);
                person.setFirstName("John");
                person.setLastName("Smith");

                RealmList<Hobby> hobbies = new RealmList<Hobby>();
                Hobby hobby1 = realm.createObject(Hobby.class);
                hobby1.setName("tv game");

                hobbies.add(hobby1);

                Hobby hobby2 = realm.createObject(Hobby.class);
                hobby2.setName("cycling");

                hobbies.add(hobby2);

                person.setHobbies(hobbies);

                return person;
            }
        }).map(new Func1<Person, PersonDto>() {
            @Override
            public PersonDto call(Person person) {
                List<HobbyDto> hobbies = Observable.from(person.getHobbies())
                        .map(new Func1<Hobby, HobbyDto>() {
                            @Override
                            public HobbyDto call(Hobby hobby) {
                                return new HobbyDto(hobby.getName());
                            }
                        }).toList().toBlocking().single();

                return new PersonDto(person.getFirstName(), person.getLastName(), hobbies);
            }
        }).toBlocking().first();

        // test
        List<HobbyDto> hobbies = RealmObservable.list(context, REALM_NAME,
                new Func1<Realm, RealmList<Hobby>>() {
                    @Override
                    public RealmList<Hobby> call(Realm realm) {
                        Person person = realm.where(Person.class).findFirst();

                        return person.getHobbies();
                    }
                }).map(new Func1<RealmList<Hobby>, List<HobbyDto>>() {
            @Override
            public List<HobbyDto> call(RealmList<Hobby> hobbies) {
                return Observable.from(hobbies)
                        .map(new Func1<Hobby, HobbyDto>() {
                            @Override
                            public HobbyDto call(Hobby hobby) {
                                return new HobbyDto(hobby.getName());
                            }
                        }).toList().toBlocking().first();
            }
        }).toBlocking().first();

        assertThat(hobbies).hasSize(2);
        assertThat(hobbies.get(0).getName()).isEqualTo("tv game");
        assertThat(hobbies.get(1).getName()).isEqualTo("cycling");
    }

    @Test
    public void RealmObservable_results() {
        Context context = InstrumentationRegistry.getTargetContext();

        // setup
        RealmObservable.object(context, REALM_NAME, new Func1<Realm, Person>() {
            @Override
            public Person call(Realm realm) {
                Person person = realm.createObject(Person.class);
                person.setFirstName("John");
                person.setLastName("Smith");

                RealmList<Hobby> hobbies = new RealmList<Hobby>();
                Hobby hobby1 = realm.createObject(Hobby.class);
                hobby1.setName("tv game");

                hobbies.add(hobby1);

                Hobby hobby2 = realm.createObject(Hobby.class);
                hobby2.setName("cycling");

                hobbies.add(hobby2);

                person.setHobbies(hobbies);

                return person;
            }
        }).map(new Func1<Person, PersonDto>() {
            @Override
            public PersonDto call(Person person) {
                List<HobbyDto> hobbies = Observable.from(person.getHobbies())
                        .map(new Func1<Hobby, HobbyDto>() {
                            @Override
                            public HobbyDto call(Hobby hobby) {
                                return new HobbyDto(hobby.getName());
                            }
                        }).toList().toBlocking().single();

                return new PersonDto(person.getFirstName(), person.getLastName(), hobbies);
            }
        }).toBlocking().first();

        List<PersonDto> people = RealmObservable.results(context, REALM_NAME,
                new Func1<Realm, RealmResults<Person>>() {
                    @Override
                    public RealmResults<Person> call(Realm realm) {
                        return realm.where(Person.class).findAll();
                    }
                }).map(new Func1<RealmResults<Person>, List<PersonDto>>() {
            @Override
            public List<PersonDto> call(RealmResults<Person> persons) {
                return Observable.from(persons)
                        .map(new Func1<Person, PersonDto>() {
                            @Override
                            public PersonDto call(Person person) {
                                List<HobbyDto> hobbies = Observable.from(person.getHobbies())
                                        .map(new Func1<Hobby, HobbyDto>() {
                                            @Override
                                            public HobbyDto call(Hobby hobby) {
                                                return new HobbyDto(hobby.getName());
                                            }
                                        }).toList().toBlocking().first();
                                return new PersonDto(person.getFirstName(), person.getLastName(),
                                        hobbies);
                            }
                        }).toList().toBlocking().first();
            }
        }).toBlocking().first();

        assertThat(people).hasSize(1);
        assertThat(people.get(0).getFirstName()).isEqualTo("John");
        assertThat(people.get(0).getLastName()).isEqualTo("Smith");
        assertThat(people.get(0).getHobbies()).hasSize(2);
        assertThat(people.get(0).getHobbies().get(0).getName()).isEqualTo("tv game");
        assertThat(people.get(0).getHobbies().get(1).getName()).isEqualTo("cycling");
    }

}
