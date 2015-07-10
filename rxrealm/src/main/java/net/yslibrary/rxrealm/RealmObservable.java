package net.yslibrary.rxrealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public class RealmObservable {

    private RealmObservable() {
        // empty private constructor
    }

    public static <T extends RealmObject> Observable<T> object(final Func1<Realm, T> function) {

        return Observable.create(new OnSubscribeRealm<T>() {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<T> object(RealmConfiguration realmConfig,
            final Func1<Realm, T> function) {

        return Observable.create(new OnSubscribeRealm<T>(realmConfig) {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmList<T>> list(
            final Func1<Realm, RealmList<T>> function) {

        return Observable.create(new OnSubscribeRealmList<T>() {
            @Override
            public RealmList<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmList<T>> list(
            RealmConfiguration realmConfig, final Func1<Realm, RealmList<T>> function) {

        return Observable.create(new OnSubscribeRealmList<T>(realmConfig) {
            @Override
            public RealmList<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmResults<T>> results(
            final Func1<Realm, RealmResults<T>> function) {

        return Observable.create(new OnSubscribeRealmResults<T>() {
            @Override
            public RealmResults<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmResults<T>> results(
            RealmConfiguration realmConfig, final Func1<Realm, RealmResults<T>> function) {

        return Observable.create(new OnSubscribeRealmResults<T>(realmConfig) {
            @Override
            public RealmResults<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }


}
