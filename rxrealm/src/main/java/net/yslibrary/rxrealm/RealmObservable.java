package net.yslibrary.rxrealm;

import android.content.Context;

import io.realm.Realm;
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

    public static <T extends RealmObject> Observable<T> object(Context context,
            final Func1<Realm, T> function) {

        return Observable.create(new OnSubscribeRealm<T>(context) {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<T> object(Context context, String dbName,
            final Func1<Realm, T> function) {

        return Observable.create(new OnSubscribeRealm<T>(context, dbName) {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmList<T>> list(Context context,
            final Func1<Realm, RealmList<T>> function) {

        return Observable.create(new OnSubscribeRealmList<T>(context) {
            @Override
            public RealmList<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmList<T>> list(Context context,
            String dbName, final Func1<Realm, RealmList<T>> function) {

        return Observable.create(new OnSubscribeRealmList<T>(context, dbName) {
            @Override
            public RealmList<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmResults<T>> results(Context context,
            final Func1<Realm, RealmResults<T>> function) {

        return Observable.create(new OnSubscribeRealmResults<T>(context) {
            @Override
            public RealmResults<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<RealmResults<T>> results(Context context,
            String dbName, final Func1<Realm, RealmResults<T>> function) {

        return Observable.create(new OnSubscribeRealmResults<T>(context, dbName) {
            @Override
            public RealmResults<T> get(Realm realm) {
                return function.call(realm);
            }
        });
    }


}
