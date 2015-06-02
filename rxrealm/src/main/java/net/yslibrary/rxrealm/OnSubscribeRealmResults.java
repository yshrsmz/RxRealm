package net.yslibrary.rxrealm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by shimizu_yasuhiro on 15/06/02.
 */
public abstract class OnSubscribeRealmResults<T extends RealmObject>
        implements Observable.OnSubscribe<RealmResults<T>> {

    private Context mContext;

    private String mDbName;

    public OnSubscribeRealmResults(Context context) {
        this(context, null);
    }

    public OnSubscribeRealmResults(Context context, String dbName) {
        mContext = context;
        mDbName = dbName;
    }

    @Override
    public void call(final Subscriber<? super RealmResults<T>> subscriber) {
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

        RealmResults<T> objects;
        realm.beginTransaction();
        try {
            objects = get(realm);
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

        if (objects != null) {
            subscriber.onNext(objects);
        }
        subscriber.onCompleted();
    }

    public abstract RealmResults<T> get(Realm realm);
}
