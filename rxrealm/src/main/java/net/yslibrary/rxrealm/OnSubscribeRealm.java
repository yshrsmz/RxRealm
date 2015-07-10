package net.yslibrary.rxrealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public abstract class OnSubscribeRealm<T extends RealmObject> implements Observable.OnSubscribe<T> {

    private RealmConfiguration mRealmConfig;

    public OnSubscribeRealm() {
        this(null);
    }

    public OnSubscribeRealm(RealmConfiguration realmConfig) {
        mRealmConfig = realmConfig;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        final Realm realm = mRealmConfig != null ?
                Realm.getInstance(mRealmConfig) : Realm.getDefaultInstance();

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                try {
                    realm.close();
                } catch (RealmException ex) {
                    subscriber.onError(ex);
                }
            }
        }));

        T object;
        realm.beginTransaction();
        try {
            object = get(realm);
            realm.commitTransaction();
        } catch (RuntimeException ex) {
            realm.cancelTransaction();
            subscriber.onError(new RealmException("Error during transaction.", ex));
            return;
        } catch (Error ex) {
            realm.cancelTransaction();
            subscriber.onError(ex);
            return;
        }
        if (object != null) {
            subscriber.onNext(object);
        }
        subscriber.onCompleted();
    }

    public abstract T get(Realm realm);
}
