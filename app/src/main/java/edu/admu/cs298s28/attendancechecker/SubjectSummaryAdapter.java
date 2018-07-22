package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
public class SubjectSummaryAdapter extends BaseAdapter {
    RealmResults<TransactionData> mySubjects;
    Activity activity;
    Realm realm;
    private Context c;
    boolean editmode = false;
    TransactionData d;
    UserData curUser;
    ScheduleData sched;

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

    public SubjectSummaryAdapter(Activity activity, TransactionData subj, RealmResults<TransactionData> realmResults) {
        this.activity = activity;
        this.mySubjects = realmResults;
        this.d = subj;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView==null) {
            view = activity.getLayoutInflater().inflate(R.layout.row_subjectsummary, null    );
        }
        else {
            view = convertView;
        }

        d = mySubjects.get(position);
        final TextView subjField = view.findViewById(R.id.txtSubject);
        final TextView descField = view.findViewById(R.id.txtDesc);
        final TextView userField = view.findViewById(R.id.txtUser);

        realm = MyRealm.getRealm();
        sched=realm.where(ScheduleData.class).equalTo("subject_id",d.getTrans_schedid()).findFirst();
        curUser=realm.where(UserData.class).equalTo("user_id",d.getTrans_userid()).findFirst();

        //System.out.println(sched.getSubject_id());

        subjField.setText(sched.getSubject_title());
        userField.setText(curUser.getName());
        descField.setText(d.getTrans_datetime());

        return view;
    }}
