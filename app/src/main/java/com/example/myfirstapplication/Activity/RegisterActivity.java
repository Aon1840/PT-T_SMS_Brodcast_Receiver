package com.example.myfirstapplication.Activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getName();
    private EditText edtFirstName, edtLastName, edtEmail;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    String code, phone, verificationId;
    private String DB_NAME = "user";
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initInstance();

        Bundle extras = getIntent().getExtras();
        phone = extras.getString("phone");

        if (edtFirstName.getText().toString() == null){
            edtFirstName.setError("Please enter first name");
            edtFirstName.requestFocus();
        }

        if (edtLastName.getText().toString() == null){
            edtLastName.setError("Please enter last name");
            edtLastName.requestFocus();
        }

        if (edtEmail.getText().toString() == null){
            edtEmail.setError("Please enter email");
            edtEmail.requestFocus();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setInfoUser(final String phone){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                User user = appDatabase.userDao().getUserByPhone(phone);
                Log.d(TAG,"----- phone from setInfoUser"+ phone);
                Log.d(TAG,"----- user from setInfoUser: "+user);
                try {
                    edtFirstName.setText(user.getFirstName());
                    edtLastName.setText(user.getLastName());
                    edtEmail.setText(user.getEmail());
                } catch (Exception e) {
                    Log.d(TAG,"----- Exception from setInfoUser: "+e.getMessage());
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtEmail.setText("");
                }
                return null;
            }
        }.execute();
    }

    private void updateUser(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(TAG,"----- phone from updateUser: "+phone);
                User user = appDatabase.userDao().getUserByPhone(phone);
                Log.d(TAG,"----- user from updateUser: "+user);
                user.setFirstName(edtFirstName.getText().toString());
                user.setLastName(edtFirstName.getText().toString());
                user.setEmail(edtEmail.getText().toString());

                appDatabase.userDao().updateUser(user);
                return null;
            }
        }.execute();
    }

    private void insertUser(){
        User user = new User();
        user.setFirstName(edtFirstName.getText().toString());
        user.setLastName(edtLastName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setPhoneNumber(phone);
        insertUserTask(user);
    }

    private void insertUserTask(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().createUser(user);
                return null;
            }
        }.execute();
        Log.d(TAG,"-------------- Success from insertUserTask");
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initInstance() {
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        appDatabase = Room.databaseBuilder(this, AppDatabase.class,DB_NAME).build();
    }
}
