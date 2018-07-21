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
public class EnrollStudentsAdapter extends BaseAdapter {
    RealmResults<UserData> theUsers;
    Activity activity;
    Realm realm;
    private Context c;
    UserData d;
    ScheduleData theSubject;

    @Override
    public int getCount() {
        return theUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return theUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public EnrollStudentsAdapter(Activity activity, ScheduleData subject, RealmResults<UserData> realmResults) {
        this.activity = activity;
        this.theUsers = realmResults;
        this.theSubject = subject;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView==null) {
            view = activity.getLayoutInflater().inflate(R.layout.row_enrollstudent, null    );
        }
        else {
            view = convertView;
        }

        d = theUsers.get(position);

        final TextView subjField = view.findViewById(R.id.txtEnrollStudent);

       // d = adapterData.get(position);

        Button btnAssign = view.findViewById(R.id.btnEnrollStudent);
        btnAssign.setTag(d);
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserData theUser = (UserData) v.getTag();

                realm = MyRealm.getRealm();
                try {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            theUser.getSubjects().add(theSubject);
                        }
                    });
                } finally {
                    realm.close();
                }
            }
        });

        String subjectName = d.getUser_id() + "\n" + d.getName();

        subjField.setText(subjectName);

        return view;
    }
}
