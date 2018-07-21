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
public class EnrollStudents extends AppCompatActivity {


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
    String subjectID;

    ScheduleData sbj;
    Activity c;

    RealmResults<UserData> d;
    EnrollStudentsAdapter a;

    @AfterViews
    public void init(){
        realm = MyRealm.getRealm();

        c = this;
        sbj = realm.where(ScheduleData.class).equalTo("subject_id",subjectID).findFirst();
        setTitle("Students to enrol for " + sbj.getSubject_title() + "(" + sbj.getSubject_desc() + ")");
        d = realm.where(UserData.class)
                    .beginGroup()
                        .not()
                        .contains("user_type", "Teacher")
                    .endGroup()
                    .and()
                    .beginGroup()
                        .not()
                        .contains("subjects.subject_id",subjectID)
                    .endGroup()
                    .findAllAsync();


        d.addChangeListener(new RealmChangeListener<RealmResults<UserData>>() {
            @Override
            public void onChange(RealmResults<UserData> students) {
                if(a == null){
                    a = new EnrollStudentsAdapter(c, sbj, d);
                    list.setAdapter(a);
                } else {
                    a.notifyDataSetChanged();
                }
            }
        });
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
