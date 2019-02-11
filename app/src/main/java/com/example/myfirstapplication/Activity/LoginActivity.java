package com.example.myfirstapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapplication.R;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private Button btnSignIn;
    private EditText edtPhone;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstance();

        phone = edtPhone.getText().toString();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePhoneNumber(phone) == true) {
                    Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                    intent.putExtra("phone",phone);
                    Log.d(TAG, "Phone number is: "+phone);
                    Toast.makeText(LoginActivity.this,"Phone number is: "+phone, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });
    }

    private void initInstance() {
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
    }

    private boolean validatePhoneNumber(String phone){
        if(phone.isEmpty() || phone.length() < 10) {
            edtPhone.setError("Enter a valid phone number");
            edtPhone.requestFocus();
            return false;
        }

        return true;
    }
}
