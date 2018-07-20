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

    @Extra
    String uid;

    UserData usr;


    @AfterViews
    public void init(){
        realm = MyRealm.getRealm();
       /* realm = MyRealm.getRealm();
        RealmResults<ScheduleData> reportsList = realm.where(ScheduleData.class).findAll();
        sa = new SubjectAdapter(this, reportsList);
        list.setAdapter(sa);*/


       /* RealmResults<ScheduleData> reportsList = realm.where(ScheduleData.class).findAll();

        SubjectAdapter s = new SubjectAdapter(this, reportsList);
        list.setAdapter(s);*/

      /*  RealmResults<ScheduleData> dat = realm.where(ScheduleData.class).findAll();
        final SubjectAdapter adapter = new SubjectAdapter(this, dat);
        //put adapter to list
        list.setAdapter(adapter);
        */

        //uid=usr.getUser_id();

        RealmResults<ScheduleData> d = realm.where(ScheduleData.class).findAll();
        SubjectAdapter a = new SubjectAdapter(this,d);
        list.setAdapter(a);
    }

    @Click(R.id.btnAdd)
    public void addSubject(){
    AddSubject_.intent(this).start();
    }

    /*@Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
