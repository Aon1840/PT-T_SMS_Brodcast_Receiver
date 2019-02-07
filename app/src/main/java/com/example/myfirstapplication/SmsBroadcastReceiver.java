package com.example.myfirstapplication;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import okhttp3.internal.http2.Http2Connection;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        String body="";
        String phone="";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                phone = smsMessage.getDisplayOriginatingAddress();
                body += smsMessage.getMessageBody();
                Log.d("TESTTTTT----", "SMS detected: From " + phone + " With text " + body);
            }

            if (body.startsWith("Hello")){
                ringtone();
                Toast.makeText(context,"SMS detected: From " + phone + " With text " + body,Toast.LENGTH_LONG).show();

                Intent intent1 = new Intent(context, TestActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("body",body);
                intent1.putExtra("phone",phone);
                context.startActivity(intent1);
            }
        } else {

            if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                return;
            }

            Bundle bundle = intent.getExtras();
            Object[] data = (Object[]) bundle.get("pdus");


            for (int i=0; i< data.length; i++){
                SmsMessage message = SmsMessage.createFromPdu((byte[]) data[i]);
                body += message.getMessageBody();
            }
            phone = SmsMessage.createFromPdu((byte[]) data[0]).getDisplayOriginatingAddress();
            Log.d("TESTTTTT----", "SMS detected: From " + phone + " With text " + body);

            if (body.startsWith("Hello")){
                ringtone();
                Toast.makeText(context,"SMS detected: From " + phone + " With text " + body,Toast.LENGTH_LONG).show();

                Intent intent1 = new Intent(context, TestActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("body",body);
                intent1.putExtra("phone",phone);
                context.startActivity(intent1);
            }
        }

    }


    //    TODO: It not working??
    public void ringtone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
