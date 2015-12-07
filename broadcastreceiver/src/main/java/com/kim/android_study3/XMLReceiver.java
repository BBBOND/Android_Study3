package com.kim.android_study3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 关闭应用程序后还可监听到
 * Created by 伟阳 on 2015/12/5.
 */
public class XMLReceiver extends BroadcastReceiver {

    public XMLReceiver() {
        Log.d("XMLReceiver", "-------->BroadcastReceiver Starting");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("XMLReceiver", "-------->BroadcastReceiver Doing");
    }
}
