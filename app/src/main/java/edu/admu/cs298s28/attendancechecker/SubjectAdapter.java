package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class SubjectAdapter extends RealmBaseAdapter <ScheduleData>  implements ListAdapter {

    /*private SubjectList activity;
    Realm realm;
    ScheduleData d;*/

    OrderedRealmCollection<ScheduleData> realmResults;
    Activity activity;
    Realm realm;

    public SubjectAdapter(Activity activity, OrderedRealmCollection<ScheduleData> realmResults) {
        super(realmResults);
        this.activity = activity;
        this.realmResults = realmResults;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //if (convertView == null) {\
       /* View view = activity.getLayoutInflater().inflate(R.layout.row_subject,null);*/
        //viewHolder = new ViewHolder();
        View view = null;
        if (convertView==null) {
            view = activity.getLayoutInflater().inflate(R.layout.row_subject, null    );
        }
        else {
            view = convertView;
        }

        ScheduleData d = realmResults.get(position);
        final TextView subjField = view.findViewById(R.id.txtSubject);
        final TextView descField = view.findViewById(R.id.txtDesc);

       // d = adapterData.get(position);

        Button btnGenerate = view.findViewById(R.id.btnGenerate);
        btnGenerate.setTag(d);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get uid + subject ID + time + date
               // GenerateQR_.intent(activity).uid(uid).start();
            }
        });

        Button btnScan = view.findViewById(R.id.btnScan);
        btnScan.setTag(d);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //triggered scan
               /* Intent intent=new Intent(this,QRScan.class);
                startActivity(intent);*/
            }
        });

        Button btnSummary = view.findViewById(R.id.btnSummary);
        btnSummary.setTag(d);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //subjectsummary summary of all the attendance made
                SubjectSummary_.intent(activity).start();
            }
        });

        Button btnEnroll= view.findViewById(R.id.btnEnroll);
        btnEnroll.setTag(d);
        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //condition if teacher log in this button will be visible else not visible and if the user is already enrolled
                //enroll student
            }
        });

        Button btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setTag(d);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit
            }
        });

        Button btnDelete= view.findViewById(R.id.btnDelete);
        btnDelete.setTag(d);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete

                realm = MyRealm.getRealm();
                RealmResults<ScheduleData> list = realm.where(ScheduleData.class).findAll();
                realm.beginTransaction();
                list.deleteFromRealm(position);
                realm.commitTransaction();
                realm.close();
            }
        });


        subjField.setText(d.getSubject_title());
        descField.setText(d.getSubject_desc());

        return view;
    }
}
