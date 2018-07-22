package edu.admu.cs298s28.attendancechecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

//Attendance Summary
@EActivity(R.layout.activity_subjectsummary)
public class SubjectSummary extends AppCompatActivity {
    @ViewById(R.id.list)
    ListView list;

    @Extra("name")
    String name;

    @Extra
    String uid;

    @Extra("us")
    String us;

    Intent intent;
    Realm realm;
    TransactionData subjsum;
    ScheduleData sched;
    UserData usr;

    RealmResults<TransactionData> d;
    SubjectSummaryAdapter a;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "userpref";

    @AfterViews
    public void init(){

        sharedpreferences = getSharedPreferences(mypreference, MODE_PRIVATE);

        realm = MyRealm.getRealm();
        subjsum = realm.where(TransactionData.class).equalTo("trans_schedid", name).findFirst();
        sched=realm.where(ScheduleData.class).equalTo("subject_id",name).findFirst();

        if(subjsum != null){
            setTitle("Attendance (" + sched.getSubject_title() + ")");

            String r = sharedpreferences.getString("urole", "");

            System.out.println(r);
            if(r.equals("Student")) {
                d = realm.where(TransactionData.class)
                        .equalTo("trans_schedid", subjsum.getTrans_schedid())
                        .and()
                        .equalTo("trans_userid", sharedpreferences.getString("uid", ""))
                        .findAll();

                a = new SubjectSummaryAdapter(this, subjsum, d);
                list.setAdapter(a);

                d.addChangeListener(new RealmChangeListener<RealmResults<TransactionData>>() {
                    @Override
                    public void onChange(RealmResults<TransactionData> userSchedules) {
                        a.notifyDataSetChanged();
                    }
                });
            }

            else{
                d = realm.where(TransactionData.class)
                        .equalTo("trans_schedid", subjsum.getTrans_schedid())
                        .findAll();

                a = new SubjectSummaryAdapter(this, subjsum, d);
                list.setAdapter(a);


                d.addChangeListener(new RealmChangeListener<RealmResults<TransactionData>>() {
                    @Override
                    public void onChange(RealmResults<TransactionData> userSchedules) {
                        a.notifyDataSetChanged();
                    }
                });
            }

        }

else {
        setTitle("Attendance (" + sched.getSubject_title() + ")");
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
