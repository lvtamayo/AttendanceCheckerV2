package edu.admu.cs298s28.attendancechecker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

@EActivity(R.layout.activity_subjectlist)
public class SubjectList extends AppCompatActivity {


    Intent intent;
    Realm realm;
    SubjectAdapter s;
    //
    // Button btnEdit;
    //public static final String mypreference = "mypref";

    @ViewById(R.id.list)
    ListView list;

    @ViewById(R.id.btnAdd)
    Button btnAdd;

    @Extra
    String uid;

    @AfterViews
    public void init(){

       /* realm = MyRealm.getRealm();
        RealmResults<ScheduleData> reportsList = realm.where(ScheduleData.class).findAll();
        sa = new SubjectAdapter(this, reportsList);
        list.setAdapter(sa);*/

        realm = MyRealm.getRealm();
        RealmResults<ScheduleData> reportsList = realm.where(ScheduleData.class).findAll();
        s = new SubjectAdapter(this, reportsList);
        list.setAdapter(s);
    }

    @Click(R.id.btnAdd)
    public void addSubject(){
    AddSubject_.intent(this).start();
    }
}
