package edu.admu.cs298s28.attendancechecker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

@EActivity(R.layout.activity_subjectlist)
public class SubjectList extends AppCompatActivity {


    Intent intent;
    Realm realm;
    //SubjectAdapter s;
    //
    // Button btnEdit;
    //public static final String mypreference = "mypref";

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.btnAdd)
    Button btnAdd;

    @ViewById(R.id.btnMaps)
    Button btnMaps;

    @Extra
    String uid;

    UserData usr;

    RealmResults<ScheduleData> d;
    SubjectAdapter a;

    @AfterViews
    public void init(){


        realm = MyRealm.getRealm();

        usr = realm.where(UserData.class).equalTo("user_id", uid).findFirst();
        if(usr != null){
            setTitle("Subjects under " + usr.getName() + "(" + usr.getUser_id() + ")");

            if(usr.getUser_type().equals("Student")){
                btnAdd.setVisibility(View.GONE);
            }

            d = realm.where(ScheduleData.class)
                    .equalTo("users.user_id",usr.getUser_id())
                    .findAll();

            a = new SubjectAdapter(this, usr, d);
            list.setAdapter(a);

            d.addChangeListener(new RealmChangeListener<RealmResults<ScheduleData>>() {
                @Override
                public void onChange(RealmResults<ScheduleData> userSchedules) {
                    a.notifyDataSetChanged();
                }
            });

        } else {
            setTitle("Subjects under (" + uid + ")");
        }
    }

    @Click(R.id.btnAdd)
    public void addSubject(){
        AssignSubject_.intent(this).uid(usr.getUser_id()).start();
        realm.close();
    }

    //added button

    @Click(R.id.btnMaps)
    public void showMaps(){
        MapsActivity_.intent(this).start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
