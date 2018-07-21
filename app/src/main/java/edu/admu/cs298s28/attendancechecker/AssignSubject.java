package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

@EActivity(R.layout.activity_list)
public class AssignSubject extends AppCompatActivity {


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

    @Extra
    String uid;

    UserData usr;
    Activity c;

    RealmResults<ScheduleData> d;
    AssignSubjectAdapter a;

    @AfterViews
    public void init(){
        realm = MyRealm.getRealm();

        c = this;

        usr = realm.where(UserData.class).equalTo("user_id", uid).findFirst();
        if(usr != null){
            setTitle("Subjects available for " + usr.getName() + "(" + usr.getUser_id() + ")");
            if(usr.getUser_type().equals("Students")) {
                d = realm.where(ScheduleData.class)
                        .not()
                        .contains("users.user_id",usr.getUser_id())
                        .findAllAsync();
            } else {
                d = realm.where(ScheduleData.class)
                        .beginGroup()
                            .not()
                            .contains("users.user_id", usr.getUser_id())
                        .endGroup()
                        .and()
                        .beginGroup()
                            .not()
                            .contains("users.user_type","Teacher")
                        .endGroup()
                        .findAllAsync();
            }

            d.addChangeListener(new RealmChangeListener<RealmResults<ScheduleData>>() {
                @Override
                public void onChange(RealmResults<ScheduleData> userSchedules) {
                    if(a == null){
                        a = new AssignSubjectAdapter(c, usr, d);
                        list.setAdapter(a);
                    } else {
                        a.notifyDataSetChanged();
                    }
                }
            });

        } else {
            setTitle("Subjects under (" + uid + ")");
        }
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
