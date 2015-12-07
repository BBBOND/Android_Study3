package com.kim.android_study3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 只在应用开启时才监听
 * 常用于更新UI等
 * Created by 伟阳 on 2015/12/5.
 */
public class CodeReceiver extends BroadcastReceiver {

    public CodeReceiver() {
        Log.d("CodeReceiver", "-------->BroadcastReceiver Starting");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CodeReceiver", "-------->BroadcastReceiver Doing");
    }
}
