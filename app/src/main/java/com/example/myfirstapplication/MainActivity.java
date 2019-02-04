package com.example.myfirstapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isSmsPermissionGranted();

        showRequestPermissionInfoAlertDialog(true);
//        SmsHelper.sendSampleSms("0882497718","Hello");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                Log.d("TEST","Grant Result/length: "+grantResults.length);
                Log.d("TEST","Grant Result[0]: "+grantResults[0]);
                Log.d("TEST","PackageManager.PERMISSION_GRANTED: "+PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permission is Grantedfsfdsfsfdfhfggdadasdasdadsdasddasdadas", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,"Permission is Not Granted", Toast.LENGTH_LONG).show();
                }
            }
            return;
        }
    }


    public void showRequestPermissionInfoAlertDialog(final boolean makeSystemRequet){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert for Requet Permission");
        builder.setMessage("Test message");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (makeSystemRequet){
                    requestReadAndSendSmsPermission();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    // Check sms permission
    public boolean isSmsPermissionGranted(){
        boolean test = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("CHECK","Permission is: "+test);
        return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    // Check permission read and send sms
    private void requestReadAndSendSmsPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            Toast.makeText(this,"Pass this condition", Toast.LENGTH_LONG).show();
        }
        Log.d("CHECK","requestReadAndSendSmsPermission is: "+Manifest.permission.READ_SMS);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }
}
