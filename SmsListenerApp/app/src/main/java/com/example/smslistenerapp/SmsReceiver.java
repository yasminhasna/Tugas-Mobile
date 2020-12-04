package com.example.smslistenerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public SmsReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null){
                final Object[] pushObj = (Object[]) bundle.get("pdus");
                for(int i = 0; i < pushObj.length; i++){
                    SmsMessage currentMessage = getIncomingMessage(pushObj[i], bundle);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNumber = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: "+senderNumber+"; message: "+message);
                    Intent showIntent = new Intent(context, SmsReceiverActivity.class);
                    showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, phoneNumber);
                    showIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message);
                    context.startActivity(showIntent);

                }
            }
        } catch (Exception e){
            Log.e("SmsReceiver", "Exception "+e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle){
        SmsMessage currentSms;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String format = bundle.getString("format");
            currentSms = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSms = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSms;
    }
}