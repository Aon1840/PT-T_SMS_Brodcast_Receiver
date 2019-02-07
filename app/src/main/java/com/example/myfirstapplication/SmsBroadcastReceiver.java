package com.example.myfirstapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import okhttp3.internal.http2.Http2Connection;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }

        Bundle bundle = intent.getExtras();
        Object[] data = (Object[]) bundle.get("pdus");

        String body = "";

        for (int i=0; i< data.length; i++){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) data[i]);
            body += message.getMessageBody();
        }

        String phone = SmsMessage.createFromPdu((byte[]) data[0]).getDisplayOriginatingAddress();
        Log.d("TESTTTTT----", "SMS detected: From " + phone + " With text " + body);

        if (body.startsWith("Hello")){
            Toast.makeText(context,"SMS detected: From " + phone + " With text " + body,Toast.LENGTH_LONG).show();
        }

    }
}
