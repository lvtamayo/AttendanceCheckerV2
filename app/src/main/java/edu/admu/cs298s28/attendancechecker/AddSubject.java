package edu.admu.cs298s28.attendancechecker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

@EActivity(R.layout.activity_addsubject)
public class AddSubject extends AppCompatActivity {

    Intent intent;
    Realm realm;
    Toast toast;
    Context c;
    boolean editMode;
    ScheduleData sched;

    @ViewById(R.id.txtSubject)
    EditText txtSubject;

    @ViewById(R.id.txtDesc)
    EditText txtDesc;

    @ViewById(R.id.txtTime)
    EditText txtTime;

    @ViewById(R.id.sprday)
    Spinner sprday;

    @ViewById(R.id.txtSY)
    EditText txtSY;

    @ViewById(R.id.txtLat)
    TextView txtLat;

    @ViewById(R.id.txtLon)
    TextView txtLon;

    @ViewById(R.id.btnAdd)
    Button btnAdd;

    @ViewById(R.id.btnCancel)
    Button btnCancel;

    @ViewById(R.id.btnMaps)
    Button btnMaps;

    @Extra
    String name;


    @AfterViews
    public void init(){
        c = this;

        if(name != null){
            editMode = true;
            realm = MyRealm.getRealm();
            sched = realm.where(ScheduleData.class).equalTo("subject_id", name).findFirst();

            txtSubject.setText(sched.getSubject_title());
            txtDesc.setText(sched.getSubject_desc());
            txtTime.setText(sched.getSubject_time());
            txtSY.setText(sched.getSubject_sy());
            txtLat.setText(sched.getSubject_lat());
            txtLon.setText(sched.getSubject_long());
            sprday.setSelection(((ArrayAdapter) sprday.getAdapter()).getPosition(sched.getSubject_day()));
            sprday.setEnabled(false);
            btnMaps.setEnabled(false);

        }
    }

    @Click(R.id.btnMaps)
    public void maps(){
        //click to go to the maps then from the MapsActivity user will set the mark
        // and will go back to this class and lat and lon will appear to the TextView and will save to the DB
        Intent intent1 = new Intent(this, MapsActivity.class);
        startActivity(intent1);
        //MapsActivity.intent(this).start();
    }

    @Click(R.id.btnCancel)
    public void cancel() {
        //close the screen back to the main screen
        onBackPressed();
    }

    @Click(R.id.btnAdd)
    public void add() {
        if (txtSubject.getText().toString().trim().length() <= 0) {
            toast = Toast.makeText(c, "Subject is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }

        if (txtDesc.getText().toString().trim().length() <= 0) {
            toast = Toast.makeText(c, "Subject Description is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }

        if (txtTime.getText().toString().trim().length() <= 0) {
            toast = Toast.makeText(c, "Time is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }


        if (txtSY.getText().toString().trim().length() <= 0) {
            toast = Toast.makeText(c, "S.Y. is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }


        if (!MyRealm.isNetworkAvailable(c)) {
            Snackbar.make(btnAdd, "No internet connection detected. Try again later."
                    , Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        //not sure how it works
        final String subj = txtSubject.getText().toString();
        final String desc = txtDesc.getText().toString();
        final String time = txtTime.getText().toString();
        final String day = sprday.getSelectedItem().toString();
        final String sy = txtSY.getText().toString();
        final String lat = txtLat.getText().toString();
        final String lon = txtLon.getText().toString();

        if(editMode){
            realm.beginTransaction();
            ScheduleData schedule = sched;
            schedule.setSubject_title(subj);
            schedule.setSubject_desc(desc);
            schedule.setSubject_time(time);
            schedule.setSubject_day(day);
            schedule.setSubject_sy(sy);
            schedule.setSubject_lat(lat);
            schedule.setSubject_long(lon);
            realm.commitTransaction();
            realm.close();

            toast = Toast.makeText(c, "Schedule info updated!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            onBackPressed();
        } else {
            /*try{
                if(SyncUser.current() != null) {
                    MyRealm.logoutUser();
                }*/

               /* SyncCredentials credentials = SyncCredentials.usernamePassword(subj, desc, true);

                SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                    @Override
                    public void onSuccess(SyncUser result) {
                        Log.e("Login Success", result.getIdentity());*/
                        realm = MyRealm.getRealm();
                        ScheduleData sched;
/*
                        if(isInUserList(uID)){
                            toast = Toast.makeText(c, "An account has been registered with this email!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        } else {*/
                        sched = new ScheduleData();
                        sched.setSubject_id(sched.getSubject_id());
                        sched.setSubject_title(subj);
                        sched.setSubject_desc(desc);
                        sched.setSubject_time(time);
                        sched.setSubject_day(day);
                        sched.setSubject_sy(sy);
                        sched.setSubject_lat(lat);
                        sched.setSubject_long(lon);

                        realm.beginTransaction();
                        realm.copyToRealm(sched);
                        realm.commitTransaction();
                        realm.close();

                        toast = Toast.makeText(c, "New schedule has been saved", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                        onBackPressed();
                    }
                    /*}*/

                    /*@Override
                    public void onError(ObjectServerError error) {
                        Log.e("Login Error", error.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                toast = Toast.makeText(this, "Cannot login to server!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            } finally {
                MyRealm.logoutUser();
            }
        }*/
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //realm.close();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
