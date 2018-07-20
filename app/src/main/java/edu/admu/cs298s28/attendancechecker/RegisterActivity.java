package edu.admu.cs298s28.attendancechecker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

@EActivity (R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    Picasso picasso;
    File savedImage;
    Intent intent;
    Realm realm;
    Toast toast;
    Context c;

    boolean editMode;
    boolean hasSavedImage;
    @Extra
    String userID;

    UserData usr;

    @ViewById(R.id.imageView)
    ImageView imageView;

    @ViewById(R.id.btnAdd)
    Button btnAdd;

    @ViewById(R.id.btnCancel)
    Button btnCancel;

    @ViewById(R.id.txtuID)
    EditText txtuID;

    @ViewById(R.id.txtname)
    EditText txtname;

    @ViewById(R.id.txtemail)
    EditText txtemail;

    @ViewById(R.id.txtpassword)
    EditText txtpassword;

    @ViewById(R.id.txtrepassword)
    EditText txtrepassword;

    @ViewById(R.id.txtcontact)
    EditText txtcontact;

    @ViewById(R.id.role)
    Spinner sprrole;

    @AfterViews
    public void init() {
        picasso = Picasso.get();
        refreshImageView(R.mipmap.ic_launcher);
        c = this;

        if(userID != null){
            editMode = true;
            realm = MyRealm.getRealm();
            usr = realm.where(UserData.class).equalTo("user_id", userID).findFirst();

            txtuID.setText(usr.getUser_id());
            txtname.setText(usr.getName());
            txtemail.setText(usr.getEmail());
            txtcontact.setText(usr.getContact_num());
            txtpassword.setText(usr.getPassword());
            txtrepassword.setText(usr.getPassword());
            sprrole.setSelection(((ArrayAdapter) sprrole.getAdapter()).getPosition(usr.getUser_type()));
            sprrole.setEnabled(false);
            if(!usr.getAvatarpath().trim().equals("")) {
                savedImage = new File(usr.getAvatarpath());
                refreshImageView(savedImage);
            }
        }
    }

    @Click(R.id.btnAdd)
    public void add(){
        if(txtuID.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "User ID is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtname.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "Name is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtemail.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "Email address is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtpassword.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "Password is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtcontact.getText().toString().trim().length() <= 0){
            toast = Toast.makeText(c, "Contact Number is required!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(txtrepassword.getText().toString().trim().length() <= 0
                || !txtpassword.getText().toString().equals(txtrepassword.getText().toString())){
            toast = Toast.makeText(c, "Passwords do not match!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            return;
        }

        if(!MyRealm.isNetworkAvailable(this)) {
            Snackbar.make(btnAdd,"No internet connection detected. Try again later."
                    ,Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        final String uID = txtuID.getText().toString();
        final String pwd = txtpassword.getText().toString();
        final String name = txtname.getText().toString();
        final String email = txtemail.getText().toString();
        final String contact = txtcontact.getText().toString();
        final String role = sprrole.getSelectedItem().toString();

        if(editMode){
            realm.beginTransaction();
            UserData user = usr;
            user.setName(name);
            user.setEmail(email);
            user.setContact_num(contact);
            user.setPassword(pwd);
            user.setUser_type(role);
            if(savedImage != null) {
                user.setAvatarpath(savedImage.getAbsolutePath());
            } else {
                user.setAvatarpath("");
            }
            realm.commitTransaction();

            toast = Toast.makeText(this, "User info updated!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            onBackPressed();
        } else {
            try{
                if(SyncUser.current() != null) {
                    MyRealm.logoutUser();
                }

                SyncCredentials credentials = SyncCredentials.usernamePassword(uID, pwd, true);

                SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                    @Override
                    public void onSuccess(SyncUser result) {
                        Log.e("Login Success", result.getIdentity());
                        realm = MyRealm.getRealm();

                        UserData user;

                        if(isInUserList(uID)){
                            toast = Toast.makeText(c, "An account has been registered with this email!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        } else {
                            user = new UserData();
                            user.setUser_id(uID);
                            user.setName(name);
                            user.setEmail(email);
                            user.setPassword(pwd);
                            user.setUser_type(role);
                            user.setContact_num(contact);
                            if (savedImage != null) {
                                user.setAvatarpath(savedImage.getAbsolutePath());
                            }else {
                                user.setAvatarpath("");
                            }

                            realm.beginTransaction();
                            realm.copyToRealm(user);
                            realm.commitTransaction();

                            toast = Toast.makeText(c, "New user has been saved", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();

                            onBackPressed();
                        }
                    }

                    @Override
                    public void onError(ObjectServerError error) {
                        Log.e("Login Error", error.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                toast = Toast.makeText(this, "Cannot login to server!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            } finally {
                MyRealm.logoutUser();
            }
        }

    }

    @Click(R.id.btnCancel)
    public void cancel() {
        //close the screen back to the main screen
        if (hasSavedImage && savedImage!=null) {
            File file = new File(savedImage.getAbsolutePath());
            if(file.delete()) {
                System.out.println("File deleted successfully");
            }
            else {
                System.out.println("Failed to delete the file");
            }
        }
        setResult(0);
        onBackPressed();
    }

    @Click(R.id.imageView)
    public void selectPic() {
        ImageActivity_.intent(this).startForResult(0);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        if (requestCode==0)
        {
            if (responseCode==100)
            {
                // save rawImage to file savedImage.jpeg
                // load file via picasso
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    savedImage = saveFile(jpeg);
                    //System.out.println(savedImage.getAbsolutePath());
                    refreshImageView(savedImage);
                    hasSavedImage = true;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    @NonNull
    private File saveFile(byte[] jpeg) throws IOException {
        File getImageDir = getExternalCacheDir();
        savedImage = new File(getImageDir, System.currentTimeMillis()+ ".jpeg");

        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }

    private void refreshImageView(File savedImage) {

        picasso.load(savedImage).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }

    private void refreshImageView(int savedImage) {

        picasso.load(savedImage).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }

    private boolean isInUserList(String userID) {
        return realm.where(UserData.class).equalTo("user_id", userID).findFirst() != null;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        /*realm.close();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
