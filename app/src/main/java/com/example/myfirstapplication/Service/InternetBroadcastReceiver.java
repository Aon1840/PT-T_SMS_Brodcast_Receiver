package com.example.myfirstapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class InternetBroadcastReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null) {
            Toast.makeText(context,"Internet is connecting",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Internet is not connecting",Toast.LENGTH_SHORT).show();
        }

    }

//    private void wifiDetection(Context context, Intent intent) {
//        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
//        switch (wifiStateExtra) {
//            case WifiManager.WIFI_STATE_ENABLED:
//                Toast.makeText(context,"Wifi is connected!!",Toast.LENGTH_LONG).show();
//                break;
//
//            case WifiManager.WIFI_STATE_DISABLED:
//                Toast.makeText(context,"Wifi is not connected!!",Toast.LENGTH_LONG).show();
//                break;
//        }
//    }
}
