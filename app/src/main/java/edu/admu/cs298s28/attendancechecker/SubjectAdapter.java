package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Context;
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
    private Context c;

    public SubjectAdapter(Activity activity, OrderedRealmCollection<ScheduleData> realmResults) {
        super(realmResults);
        this.activity = activity;
        this.realmResults = realmResults;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
                UserData usr = new UserData();
                String uid = usr.getUser_id();
                System.out.println(uid);
                //get uid + subject ID + time + date
               GenerateQR_.intent(activity).uid(uid).start();
            }
        });

        Button btnScan = view.findViewById(R.id.btnScan);
        btnScan.setTag(d);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //triggered scan
               Intent intent=new Intent(activity,QRScan.class);
                v.getContext().startActivity(intent);
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
                realm = MyRealm.getRealm();
                RealmResults<ScheduleData> list = realm.where(ScheduleData.class).findAll();
                String subjid = list.get(position).getSubject_id();
                System.out.println(subjid);
                AddSubject_.intent(activity).name(subjid).start();

                realm.close();
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
