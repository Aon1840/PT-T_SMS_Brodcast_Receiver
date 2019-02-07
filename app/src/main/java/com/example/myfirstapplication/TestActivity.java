package com.example.myfirstapplication;

import android.content.Intent;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initInstance();

        Bundle extras = getIntent().getExtras();
        String body = extras.getString("body");
        String phone = extras.getString("phone");

        tvTest.setText("From: "+phone+"\n"+"Message: "+body);
    }


    private void initInstance() {
        tvTest = (TextView) findViewById(R.id.tvTest);
    }
}
