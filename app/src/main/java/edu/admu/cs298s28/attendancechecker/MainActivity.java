package edu.admu.cs298s28.attendancechecker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

//Login Screen
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    Toast toast;
    Realm realm;
    Context c;
    RealmResults<UserData> user;
    String userID;
    String pword;
    SharedPreferences prefs;

    @ViewById(R.id.txtuID)
    EditText txtuID;

    @ViewById(R.id.txtPass)
    EditText txtPass;

    @ViewById(R.id.rem)
    Switch rem;

    @ViewById(R.id.btnLogin)
    Button btnLogin;

    @ViewById(R.id.btnRegister)
    Button btnRegister;

    @AfterViews
    public void init(){
        c = this;
        prefs = getSharedPreferences("UserData",MODE_PRIVATE);
        if (prefs.getBoolean("RememberMe", false)) {
            txtuID.setText(prefs.getString("LastUser", null));
            txtPass.setText(prefs.getString("LastPass", null));
            rem.setChecked(true);
        }
    }



    @Click(R.id.btnRegister)
    public void register(){
        RegisterActivity_.intent(this).start();
    }


    @Click(R.id.btnLogin)
    public void login(){
        if(txtuID.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "User ID is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtPass.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "Password is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(!MyRealm.isNetworkAvailable(this)) {
            Snackbar.make(btnLogin,"No internet connection detected. Try again later."
                    ,Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        userID = txtuID.getText().toString();
        pword = txtPass.getText().toString();

        SharedPreferences.Editor editor = prefs.edit();
        if (rem.isChecked()) {
            editor.putString("LastUser", userID);
            editor.putString("LastPass", pword);
            editor.putBoolean("RememberMe", true);
            editor.apply();
        }
        else {
            editor.putBoolean("RememberMe", false);
            editor.commit();
        }

        try {
            GoToNextScreen();

        }catch(Exception e) {
            e.printStackTrace();
            toast = Toast.makeText(this, "Cannot login to the server!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    private void GoToNextScreen(){
        realm = MyRealm.getRealm();

        user = realm.where(UserData.class)
                .equalTo("user_id", userID)
                .and()
                .equalTo("password",pword)
                .findAllAsync();

        user.addChangeListener(new RealmChangeListener<RealmResults<UserData>>() {
            @Override
            public void onChange(RealmResults<UserData> userg) {
                Log.e("Updating User", "Changes detected.");

                Log.d("User Valid?",user.isValid() + "");
                boolean found = false;
                if(userg.size() <= 0) {
                    toast = Toast.makeText(c, "Wrong username or password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    realm.close();
                    return;
                }
                for(UserData u: userg) {
                    if (u.isValid()) {
                        switch (u.getUser_type()) {
                            case "Student":
                                UserAccount_.intent(c).uid(u.getUser_id()).start();
                                found = true;
                                break;
                            case "Teacher":
                                UserAccount_.intent(c).uid(u.getUser_id()).start();
                                found = true;
                                break;
                        }
                        if(found){
                            realm.close();
                            break;
                        }
                    }
                }

            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        prefs = getSharedPreferences("UserData",MODE_PRIVATE);
        if (prefs.getBoolean("RememberMe", false)) {
            txtuID.setText(prefs.getString("LastUser", null));
            txtPass.setText(prefs.getString("LastPass", null));
            rem.setChecked(true);
        }
        else{
            txtuID.setText("");
            txtPass.setText("");
            rem.setChecked(false);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyRealm.logoutUser();
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //realm.close();
    }
}
