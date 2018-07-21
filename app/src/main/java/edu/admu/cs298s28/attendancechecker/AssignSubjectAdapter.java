package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

//public class SubjectAdapter extends RealmBaseAdapter <UserSchedule>  implements ListAdapter {
public class AssignSubjectAdapter extends BaseAdapter {
    RealmResults<ScheduleData> theSubjects;
    Activity activity;
    Realm realm;
    private Context c;
    ScheduleData d;
    UserData theUser;

    @Override
    public int getCount() {
        return theSubjects.size();
    }

    @Override
    public Object getItem(int position) {
        return theSubjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public AssignSubjectAdapter(Activity activity, UserData user, RealmResults<ScheduleData> realmResults) {
        this.activity = activity;
        this.theSubjects = realmResults;
        this.theUser = user;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView==null) {
            view = activity.getLayoutInflater().inflate(R.layout.row_assignsubject, null    );
        }
        else {
            view = convertView;
        }

        d = theSubjects.get(position);

        final TextView subjField = view.findViewById(R.id.txtAssignSubject);
        final TextView descField = view.findViewById(R.id.txtAssignDesc);

       // d = adapterData.get(position);

        Button btnAssign = view.findViewById(R.id.btnAssignToUser);
        btnAssign.setTag(d);
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ScheduleData theSubject = (ScheduleData) v.getTag();
                final Button thisButton = (Button) v;

                realm = MyRealm.getRealm();
                try {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            theUser.getSubjects().add(theSubject);
                            thisButton.setEnabled(false);
                            String caption = "Assigned To " + theUser.getName();
                            thisButton.setText(caption);
                        }
                    });
                } finally {
                    realm.close();
                }
            }
        });

        String subjectName = d.getSubject_title() + " - " + d.getSubject_desc();
        String subjectDesc = d.getSubject_day() + " " + d.getSubject_time() + "\n"
                + d.getSubject_sy() + "\n"
                + d.getSubject_lat() + ", " + d.getSubject_long();

        subjField.setText(subjectName);
        descField.setText(subjectDesc);

        return view;
    }
}
