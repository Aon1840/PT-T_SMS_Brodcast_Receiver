package com.example.myfirstapplication;

import android.telephony.SmsManager;
import android.util.Patterns;

public class SmsHelper {
    public static final String SMS_CONDITION = "Hello";

    public static boolean isValidPhoneNumber(String number){
        return Patterns.PHONE.matcher(number).matches();
    }

    public static void sendSampleSms(String number, String message){
        SmsManager.getDefault().sendTextMessage(number,null,message,null,null);

    }

}
