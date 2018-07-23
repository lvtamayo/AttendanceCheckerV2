package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import io.realm.Realm;
import io.realm.RealmResults;

//public class SubjectAdapter extends RealmBaseAdapter <UserSchedule>  implements ListAdapter {
public class SubjectAdapter extends BaseAdapter {
    RealmResults<ScheduleData> mySubjects;
    Activity activity;
    Realm realm;
    private Context c;
    boolean editmode = false;
    ScheduleData d;
    UserData curUser;

    TransactionData trans;

    @Override
    public int getCount() {
        return mySubjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mySubjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public SubjectAdapter(Activity activity, UserData user, RealmResults<ScheduleData> realmResults) {
        this.activity = activity;
        this.mySubjects = realmResults;
        this.curUser = user;
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

        d = mySubjects.get(position);

        final TextView subjField = view.findViewById(R.id.txtSubject);
        final TextView descField = view.findViewById(R.id.txtDesc);

       // d = adapterData.get(position);

        Button btnGenerate = view.findViewById(R.id.btnGenerate);
        btnGenerate.setTag(d);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleData curSubject = (ScheduleData) v.getTag();
                GenerateQR_.intent(activity)
                        .uid(curUser.getUser_id())
                        .name(curSubject.getSubject_id())
                        .start();
            }
        });

        Button btnScan = view.findViewById(R.id.btnScan);
        btnScan.setTag(d);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //triggered scan
                ScheduleData curSubject = (ScheduleData) v.getTag();
               Intent intent=new Intent(activity,QRScan.class);
               intent.putExtra("curUser", curUser.getUser_id());
               intent.putExtra("curSubject", curSubject.getSubject_id());
               activity.startActivity(intent);
            }
        });

        Button btnSummary = view.findViewById(R.id.btnSummary);
        btnSummary.setTag(d);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //subjectsummary summary of all the attendance made
                ScheduleData curSubject = (ScheduleData) v.getTag();
                //TransactionData curSched = (TransactionData) v.getTag();
                SubjectSummary_.intent(activity)
                        .name(curSubject.getSubject_id())
                        .uid(curUser.getUser_id())
                        .start();
            }
        });


        if(curUser.getUser_type().equals("Teacher")) {

            Button btnEdit = view.findViewById(R.id.btnEdit);
            btnEdit.setTag(d);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit
                    ScheduleData curSubject = (ScheduleData) v.getTag();
                    AddSubject_.intent(activity).name(curSubject.getSubject_id()).start();
                }
            });

            Button btnEnroll = view.findViewById(R.id.btnEnroll);
            btnEnroll.setTag(d);
            btnEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //condition if teacher log in this button will be visible else not visible and if the user is already enrolled
                    //enroll student
                    ScheduleData curSubject = (ScheduleData) v.getTag();
                    EnrollStudents_.intent(activity).subjectID(curSubject.getSubject_id()).start();
                }
            });

            Button btnDelete = view.findViewById(R.id.btnDelete);
            btnDelete.setTag(d);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete
                    ScheduleData curSubject = (ScheduleData) v.getTag();
                    realm = MyRealm.getRealm();
                    realm.beginTransaction();
                    curUser.getSubjects().remove(curSubject);
                    realm.commitTransaction();
                    realm.close();
                }
            });
        } else {

            Button btnEdit = view.findViewById(R.id.btnEdit);
            btnEdit.setVisibility(View.GONE);

            Button btnEnroll = view.findViewById(R.id.btnEnroll);
            btnEnroll.setVisibility(View.GONE);

            Button btnDelete = view.findViewById(R.id.btnDelete);
            btnDelete.setVisibility(View.GONE);
        }

        subjField.setText(d.getSubject_title());
        descField.setText(d.getSubject_desc());

        return view;
    }
}
