package com.example.myfirstapplication.Activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapplication.Database.AppDatabase;
import com.example.myfirstapplication.Database.User;
import com.example.myfirstapplication.R;
import com.example.myfirstapplication.Service.SmsBroadcastReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private TextView tvHello;
    private Button btnLogout;
    private WifiManager wifiManager;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private String fcmToken;
    private FirebaseAuth mAuth;

    private String DB_NAME = "user";
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TEST----", "SDK is: "+Build.VERSION.SDK_INT);
        Log.d(TAG, "-----------Uid from firebase is: "+FirebaseAuth.getInstance().getUid());
        initInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
//        insertUser();
//        getUser();
//        getUserName();
    }

    private void getUserName() {
        new AsyncTask<List<User>, Void, List<User>>(){
            @Override
            protected List<User> doInBackground(List<User>... users) {
                List<User> user = appDatabase.userDao().getUserByName("Attapon");
                for (int i=0; i<user.size(); i++){
                    Log.d(TAG,"======= Success from getUserByName: "+user.get(i).getUid());
                }
                return user;
            }
        }.execute();
    }


    private void getUser() {
        new AsyncTask<List<User>, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(List<User>... lists) {
                List<User> users = appDatabase.userDao().getAll();
                for (int i=0; i<users.size(); i++){
                    Log.d(TAG,"--------- Success from getUser: "+users.get(i).getUid()+" "+users.get(i).getFirstName()+" "+users.get(i).getLastName());
                }
                return users;
            }
        }.execute();
    }

    private void insertUser() {
        User user = new User();
        user.setFirstName("Attaporn");
        user.setLastName("Peungsook");
//        insertTask(user);
    }

    public void insertTask(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().createUser(user);
                return null;
            }
        }.execute();
        Log.d(TAG,"-------------- Success from insertTask");
    }

    private void initInstance() {
        btnLogout = (Button) findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"Current User:"+currentUser);

        appDatabase = Room.databaseBuilder(this,AppDatabase.class,DB_NAME).build();

        registerReceiver(smsBroadcastReceiver, new IntentFilter("android.provider.Telophony.SMS_RECEIVED"));

        initFcmToken();
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
}
