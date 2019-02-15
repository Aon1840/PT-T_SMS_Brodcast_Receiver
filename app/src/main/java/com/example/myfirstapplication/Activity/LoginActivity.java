package com.example.myfirstapplication.Activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapplication.Database.AppDatabase;
import com.example.myfirstapplication.Database.User;
import com.example.myfirstapplication.R;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private Button btnSignIn;
    private EditText edtPhone;
    private String phone;
    private String DB_NAME = "user";
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstance();

        getUser();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edtPhone.getText().toString();
                if (phone.isEmpty() || phone.length() < 10){
                    edtPhone.setError("Invalid Phone number");
                    edtPhone.requestFocus();
                    return;
                }

                String phoneNumber = "+66"+phone;

                SharedPreferences prefs = getBaseContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("phone",phoneNumber);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                intent.putExtra("phone",phoneNumber);
                Log.d(TAG, "Phone number is: "+phone);
                Toast.makeText(LoginActivity.this,"Phone number is: "+phone, Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });

    }

    private void deleteUser(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().deleteUser();
                return null;
            }
        }.execute();
    }

    private void getUser() {
        new AsyncTask<List<User>, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(List<User>... lists) {
                List<User> users = appDatabase.userDao().getAll();
                for (int i=0; i<users.size(); i++){
                    Log.d(TAG,"--------- Success from getUser: "+users.get(i).getUid()+" "+users.get(i).getFirstName()+" "+users.get(i).getLastName()+" "+users.get(i).getPhoneNumber()+" "+users.get(i).getEmail());
                }
                return users;
            }
        }.execute();
    }

    private void initInstance() {
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        edtPhone = (EditText) findViewById(R.id.edtPhone);

        appDatabase = Room.databaseBuilder(this,AppDatabase.class,DB_NAME).build();
    }

}
