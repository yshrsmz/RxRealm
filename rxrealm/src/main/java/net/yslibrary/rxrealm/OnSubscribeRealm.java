package net.yslibrary.rxrealm;

import android.content.Context;

import io.realm.Realm;
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

    private Context mContext;

    private String mDbName;

    public OnSubscribeRealm(Context context) {
        this(context, null);
    }

    public OnSubscribeRealm(Context context, String dbName) {
        mContext = context;
        mDbName = dbName;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        final Realm realm = mDbName != null ?
                Realm.getInstance(mContext, mDbName) : Realm.getInstance(mContext);

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