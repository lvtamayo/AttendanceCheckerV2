package edu.admu.cs298s28.attendancechecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import javax.security.auth.login.LoginException;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class MyRealm {

    public static SyncUser syncUser;
    //private static final String path = "/attendance_checker";
    private static final String path = "/qr_attendance";

    public static Realm getRealm(){
        SyncConfiguration configuration;

        configuration = SyncUser.current()
                .createConfiguration(Constants.REALM_INSTANCE_URL + path)
                .fullSynchronization()
                .build();
        return Realm.getInstance(configuration);
    }

    public static void logoutUser(){
        if(SyncUser.current() != null) {
            SyncUser.current().logOut();
        }
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
