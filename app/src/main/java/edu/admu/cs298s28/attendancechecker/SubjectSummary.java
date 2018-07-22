package edu.admu.cs298s28.attendancechecker;

import android.content.Intent;
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

    Intent intent;
    Realm realm;
    TransactionData subjsum;

    ScheduleData sched;

    RealmResults<TransactionData> d;
    SubjectSummaryAdapter a;

    @AfterViews
    public void init(){
        realm = MyRealm.getRealm();


subjsum = realm.where(TransactionData.class).equalTo("trans_schedid", name).findFirst();

sched=realm.where(ScheduleData.class).equalTo("subject_id",name).findFirst();

if(subjsum != null){
    setTitle("Attendance (" + sched.getSubject_title() + ")");

    d = realm.where(TransactionData.class)
            .equalTo("trans_schedid",subjsum.getTrans_schedid())
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

else {
        setTitle("Attendance (" + name + ")");
    }
       /* usr = realm.where(UserData.class).equalTo("user_id", uid).findFirst();
        if(usr != null){
            setTitle("Subjects under " + usr.getName() + "(" + usr.getUser_id() + ")");

            if(usr.getUser_type().equals("Student")){
                //btnAdd.setVisibility(View.GONE);
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
        }*/
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
