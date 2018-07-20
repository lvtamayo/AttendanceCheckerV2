package edu.admu.cs298s28.attendancechecker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.realm.Realm;

@EActivity(R.layout.activity_user_account)
public class UserAccount extends AppCompatActivity {



  /*  @Click(R.id.button)
    public void generateQR(){
        GenerateQR_.intent(this).start();
    }

    public void readQr(View view){
        Intent intent=new Intent(this,QRScan.class);
        startActivity(intent);
    }
*/

    @ViewById(R.id.imageView)
    ImageView imageView;
    @ViewById(R.id.txtuID)
    TextView txtuID;
    @ViewById(R.id.txtName)
    TextView txtName;
    @ViewById(R.id.txtEmail)
    TextView txtEmail;
    @ViewById(R.id.txtContact)
    TextView txtContact;

    @ViewById(R.id.btnUpdateAccount)
    Button btnupdateAccount;

    @ViewById(R.id.btnList)
    Button btnList;

    Realm realm;
    Picasso picasso;
    File savedImage;
    Intent intent;
    Toast toast;
    Context c;
    boolean editMode;
    boolean hasSavedImage;

    @Extra
    String uid;
    UserData usr;

    @AfterViews
    public void init(){
        picasso = Picasso.get();

        realm = MyRealm.getRealm();
        usr = realm.where(UserData.class).equalTo("user_id", uid).findFirst();

        txtuID.setText(usr.getUser_id());
        txtName.setText(usr.getName());
        txtEmail.setText(usr.getEmail());
        txtContact.setText(usr.getContact_num());
        savedImage = new File(usr.getAvatarpath());

        if(savedImage.exists()){
            refreshImageView(savedImage);
        }

     /*   switch (usr.getUser_type()) {
            case "Student":
                btnSchedule.setVisibility(View.GONE);
                break;
            case "Teacher":
                btnSchedule.setVisibility(View.VISIBLE);
                break;
        }
*/
    }
    @Click(R.id.btnUpdateAccount)
    public void updateAccount(){
        RegisterActivity_.intent(this).userID(uid).start();
    }

    @Click(R.id.btnList)
    public void summary (){
        SubjectList_.intent(this).uid(uid).start();
    }

    private void refreshImageView(File savedImage) {

        picasso.load(savedImage).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }

    private void refreshImageView(int savedImage) {

        picasso.load(savedImage).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }
}
