package edu.admu.cs298s28.attendancechecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScan extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String TAG="QRREADER";

    String curUser;
    String curSubject;
    Realm realm;
    Toast toast;
    Context c;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };

        c = this;
        setContentView(mScannerView);                // Set the scanner view as the content view
        Intent intent = getIntent();
        curUser = intent.getStringExtra("curUser");
        curSubject = intent.getStringExtra("curSubject");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // call the alert dialog
        final String[] scannedText = rawResult.getText().split(",");
        realm = MyRealm.getRealm();
        try {
            realm.beginTransaction();
            UserData theUser = realm.where(UserData.class)
                    .equalTo("user_id",curUser)
                    .findFirst();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String strDateTime = datetimeformat.format(calendar.getTime());

            TransactionData transactionData;

            if(theUser.getUser_type().equals("Teacher")){
                if(curSubject.equals(scannedText[1])) {
                    transactionData = new TransactionData();
                    transactionData.setTrans_id(UUID.randomUUID().toString());
                    transactionData.setTrans_userid(scannedText[0]);
                    transactionData.setTrans_teacherid(theUser.getUser_id());
                    transactionData.setTrans_schedid(scannedText[1]);
                    transactionData.setTrans_datetime(strDateTime);

                    UserData student = realm.where(UserData.class)
                            .equalTo("user_id", scannedText[0])
                            .findFirst();

                    student.getAttendance().add(transactionData);

                    Alert(student.getUser_id(), theUser.getUser_id(), scannedText[1]);
                } else {
                    toast = Toast.makeText(c, "Scanned QR Code's subject ID doesn't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    onResume();
                }
            } else {
                if(curSubject.equals(scannedText[1])) {
                    transactionData = new TransactionData();
                    transactionData.setTrans_id(UUID.randomUUID().toString());
                    transactionData.setTrans_userid(theUser.getUser_id());
                    transactionData.setTrans_teacherid(scannedText[0]);
                    transactionData.setTrans_schedid(scannedText[1]);
                    transactionData.setTrans_datetime(strDateTime);

                    theUser.getAttendance().add(transactionData);

                    Alert(theUser.getUser_id(), scannedText[0], scannedText[1]);
                } else {
                    toast = Toast.makeText(c, "Scanned QR Code's subject ID doesn't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    onResume();
                }
            }
            realm.commitTransaction();
        } catch(Exception e){
            realm.cancelTransaction();
            e.printStackTrace();
            Log.e("Realm Transaction", e.getMessage());
        } finally {
            realm.close();
        }

    }

    public void Alert(String student, String teacher, String subject){

        AlertDialog.Builder builder = new AlertDialog.Builder(QRScan.this);
        builder.setTitle("Qr scan result");

     /*   String uid = rawResult.getText().substring(0,9);
        String time = rawResult.getText().substring(9,17);
        String date = rawResult.getText().substring(17,27);*/


        builder.setMessage("Attendance saved for:" + "\n\n"
                + "Student: " + student + "\n"
                + "under Teacher ID: " + teacher + "\n"
                + "for Subject ID: " + subject + "\n\n"
                + "Do you want to scan another?")


                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // back to previous activity
                        //save to TransactionData
                        mScannerView.resumeCameraPreview(QRScan.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
// If you would like to resume scanning, call this method below:
                        finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    public void Alert(Result rawResult){

        AlertDialog.Builder builder = new AlertDialog.Builder(QRScan.this);
        builder.setTitle("Qr scan result");

     /*   String uid = rawResult.getText().substring(0,9);
        String time = rawResult.getText().substring(9,17);
        String date = rawResult.getText().substring(17,27);*/


        builder.setMessage("Student ID:"+rawResult.getText()+
                "\nType :"+rawResult.getBarcodeFormat().toString())


                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // back to previous activity
                        //save to TransactionData
                        finish();

                    }
                })
                .setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
// If you would like to resume scanning, call this method below:
                        mScannerView.resumeCameraPreview(QRScan.this);
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private class CustomViewFinderView extends ViewFinderView {
        public CustomViewFinderView(Context c){
            super(c);
        }
        @Override
        public Rect getFramingRect() {
            Rect originalRect = super.getFramingRect();
            return new Rect(originalRect.left
                    , originalRect.top - 50
                    , originalRect.right
                    , originalRect.bottom + 150 - 50);
        }

        @Override
        public void drawViewFinderBorder(Canvas canvas) {
            canvas.clipRect(getFramingRect());
            //canvas.clipOutRect(getFramingRect());
            super.drawViewFinderBorder(canvas);
        }
    }
}
