package com.example.myfirstapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapplication.R;
import com.example.myfirstapplication.Service.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private TextView tvHello;
    private Button btnLogin;
    private WifiManager wifiManager;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private String fcmToken;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TEST----", "SDK is: "+Build.VERSION.SDK_INT);

        registerReceiver(smsBroadcastReceiver, new IntentFilter("android.provider.Telophony.SMS_RECEIVED"));
        initInstance();
        initFcmToken();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"Current User:"+currentUser);

//        mAuth.createUserWithEmailAndPassword("aonattapon1840.ap@gmail.com", "12345679")
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG,"createUserWithEmail: Success!");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(MainActivity.this,"User: "+user,Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
            }
        });


    }

    private void initInstance() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void initFcmToken() {
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

                        tvHello = (TextView) findViewById(R.id.tvHello);
                        tvHello.setText(fcmToken);

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
