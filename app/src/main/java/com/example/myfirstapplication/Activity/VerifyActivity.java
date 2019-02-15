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
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    private static final String TAG = VerifyActivity.class.getName();

    private EditText edtOtp;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String DB_NAME = "user";
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        initInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtOtp.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    edtOtp.setError("Enter OTP Code");
                    edtOtp.requestFocus();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String phone = prefs.getString("phone",null);
                Log.d(TAG, "----- phone from verify: "+phone);

                if (phone != null) {
                    verifyCode(code);
                }

            }
        });
    }

    private void sendVerificationCode(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(VerifyActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edtOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG,"------FAILED: "+e.getMessage());
            Toast.makeText(VerifyActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void insertUserTask(final User user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                User user = new User();
                user.setPhoneNumber(getIntent().getStringExtra("phone"));
                appDatabase.userDao().createUser(user);
                return null;
            }
        }.execute();
        Log.d(TAG,"-------------- Success from insertUserTask");
    }

    private void initInstance() {
        edtOtp = (EditText) findViewById(R.id.edtOtp);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        String phone = getIntent().getStringExtra("phone");
        sendVerificationCode(phone);
    }

}
