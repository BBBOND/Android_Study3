package com.kim.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button openwifi;
    private Button closewifi;
    private WifiManager wifiManager = null;

    Handler checkWIFIHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String state = "";
            switch (msg.arg1) {
                case WifiManager.WIFI_STATE_DISABLED:
                    state = "WIFI 已关闭";
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    state = "WIFI 正在关闭...";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    state = "WIFI 已开启";
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    state = "WIFI 正在启动...";
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    state = "未知错误";
                    break;
                default:
                    state = "无法检测状态";
                    break;
            }
            setTitle(state);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openwifi = (Button) findViewById(R.id.open_wifi);
        closewifi = (Button) findViewById(R.id.close_wifi);

        openwifi.setOnClickListener(this);
        closewifi.setOnClickListener(this);

        checkWIFI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_wifi:
                openWIFI();
                break;
            case R.id.close_wifi:
                closeWIFI();
                break;
        }
    }

    public void openWIFI() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        Log.d("MainActivity", "WIFI Open-------->" + wifiManager.getWifiState());
    }

    public void closeWIFI() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Log.d("MainActivity", "WIFI Close-------->" + wifiManager.getWifiState());
    }

    public void checkWIFI() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d("MainActivity", "WIFI State-------->" + wifiManager.getWifiState());
                    Message message = Message.obtain();
                    message.arg1 = wifiManager.getWifiState();
                    checkWIFIHandler.sendMessage(message);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
