package com.example.myfirstapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    private final String CH1 = "CH1";
    int idNotiPayload = createId();
    String sn;

    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chanel Test 1";
            String description = "Chenel for test 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CH1, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("getFrom", "From: "+remoteMessage.getFrom());


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d("SDK","SDK VERSION IS: "+ Build.VERSION.SDK_INT);

            if (remoteMessage.getData() != null) {
                Log.d("MESSAGE","Pass Data Message");

                Map<String, String> data = remoteMessage.getData();
                sn = data.get("test");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CH1);
                builder.setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("MyFirstApplication")
                        .setContentText(sn)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(idNotiPayload, builder.build());

                Log.d("TEST MESSAGE---------", "Message is: " + sn);

                if (sn.startsWith("Hello")) {
                    Intent intent1 = new Intent(getApplicationContext(), TestActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent1);
                }
            }
        }

//        if (remoteMessage.getNotification() != null) {
//            Log.d("MESSAGE", "Pass Notifiaction Message");
//            sn = remoteMessage.getNotification().getBody();
//        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CH1);
//        builder.setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("MyFirstApplication")
//                .setContentText(sn)
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_SOUND);
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(idNotiPayload, builder.build());
//
//        Log.d("TEST MESSAGE---------", "Message is: " + sn);
//
//        if (sn.startsWith("Hello")) {
//            Intent intent1 = new Intent(getApplicationContext(), TestActivity.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplicationContext().startActivity(intent1);
//        }
    }

    private int createId() {
        Date now = new Date();
        int id = Integer.parseInt(new java.text.SimpleDateFormat("ddHHmmss", Locale.US).format(now));

        return id;
    }



}
