package edu.admu.cs298s28.attendancechecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_subjectsummary)
public class SubjectSummary extends AppCompatActivity {
    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    public void init(){

    }
}
