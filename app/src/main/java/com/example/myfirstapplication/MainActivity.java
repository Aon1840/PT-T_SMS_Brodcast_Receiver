package com.example.myfirstapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvHello;
    private WifiManager wifiManager;
    private static final int SMS_PERMISSION_CODE = 0;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private String fcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TEST----", "SDK is: "+Build.VERSION.SDK_INT);

        registerReceiver(smsBroadcastReceiver, new IntentFilter("android.provider.Telophony.SMS_RECEIVED"));


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            Toast.makeText(MainActivity.this, "getInstanceId failed: " + task.getException(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Get new instance ID token
                        fcmToken = task.getResult().getToken();
                        Toast.makeText(MainActivity.this, "getInstanceId Token: " + fcmToken, Toast.LENGTH_LONG).show();
                        Log.d("TAG-------------", "token: " + fcmToken);

                    }
                });

    }

    private void isConnectedInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Intent intent2;


        if (connectivityManager.getActiveNetworkInfo() != null
        ) {
            Toast.makeText(this,"Internet is connecting",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Internet is not connecting",Toast.LENGTH_LONG).show();
        }
    }
}
