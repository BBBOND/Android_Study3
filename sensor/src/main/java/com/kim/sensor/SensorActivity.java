package com.kim.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SensorActivity extends AppCompatActivity {

    @Bind(R.id.sensor_list)
    TextView sensorList;
    @Bind(R.id.show_sensor_list)
    Button showSensorList;
    @Bind(R.id.sensor_value)
    TextView sensorValue;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        ButterKnife.bind(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //显示所有传感器名及版本
        showSensorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
                String string = "";
                for (Sensor sensor : sensors) {
                    string += sensor.getName() + "----->version" + sensor.getVersion() + "\n";
                }
                sensorList.setText(string);
            }
        });

        //注册传感器及获取传感器精度和值
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(new SensorEventListener() {

            //传感器的值发生变化时调用
            @Override
            public void onSensorChanged(SensorEvent event) {
                String sensorName = event.sensor.getName();
                String sensorVendor = event.sensor.getVendor();
                float resolution = event.sensor.getResolution();
                float power = event.sensor.getPower();
                float timestamp = event.timestamp;
                float acc = event.accuracy;
                float lux = event.values[0];
                String value = "sensorName="+sensorName+"\nsensorVendor="+sensorVendor
                        +"\nresolution="+resolution+"\npower="+power+"\ntimestamp="+timestamp
                        +"\nacc=" + acc + "\nlux=" + lux;
                sensorValue.setText(value);
            }

            //传感器的精度发生变化时调用
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, lightSensor, SensorManager.SENSOR_DELAY_GAME);
        //SensorManager.SENSOR_DELAY_NORMAL = 200000ms
        //SensorManager.SENSOR_DELAY_UI = 60000ms
        //SensorManager.SENSOR_DELAY_GAME = 20000ms
        //SensorManager.SENSOR_DELAY_FASTEST = 0ms
    }
}
