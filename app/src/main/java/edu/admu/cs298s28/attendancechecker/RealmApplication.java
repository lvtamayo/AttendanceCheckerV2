package edu.admu.cs298s28.attendancechecker;

import android.app.Application;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RealmApplication extends Application {
    private final static String userID = "aris.gail";
    private final static String pword = "password";

    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        SyncCredentials credentials;
        if(MyRealm.syncUser == null) {
            credentials = SyncCredentials.usernamePassword(userID, pword, false);
            SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                @Override
                public void onSuccess(SyncUser result) {
                    MyRealm.syncUser = result;
                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.e("Status", "Cannot login to the server");
                }
            });
        }
    }
}
