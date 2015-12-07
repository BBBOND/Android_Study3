package com.kim.android_study3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by 伟阳 on 2015/12/5.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SMSReceiver", "receiver message");

        Bundle bundle = intent.getExtras();
        Object[] myOBJpdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
        for (int i = 0; i < myOBJpdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) myOBJpdus[i]);
            Log.d("SMSReceiver", "------>" + messages[i].getDisplayOriginatingAddress());
            Log.d("SMSReceiver", "------>" + messages[i].getDisplayMessageBody());
        }

    }
}
