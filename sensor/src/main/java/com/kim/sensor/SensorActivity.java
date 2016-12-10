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
    @Bind(R.id.sensor_light)
    TextView sensorLight;
    @Bind(R.id.sensor_accelerometer)
    TextView sensorAccelerometer;
    @Bind(R.id.sensor_orientation)
    TextView sensorOrientation;
    @Bind(R.id.sensor_proximity)
    TextView sensorProximity;

    private SensorManager sensorManager;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];

    private SensorEventListener lightListener;
    private SensorEventListener accelerometerListener;
    private SensorEventListener orientationListener;
    private SensorEventListener proximityListener;

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
                    string += sensor.getName() + "--->version" + sensor.getVersion() + "--->power" + sensor.getPower() + "\n";
                }
                sensorList.setText(string);
            }
        });

        init();
    }

    public void init() {
        lightListener = new SensorEventListener() {
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
                String value = "光线传感器:\nsensorName=" + sensorName + "\nsensorVendor=" + sensorVendor
                        + "\nresolution=" + resolution + "\npower=" + power + "\ntimestamp=" + timestamp
                        + "\nacc=" + acc + "\nlux=" + lux;
                sensorLight.setText(value);
            }

            //传感器的精度发生变化时调用
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float alpha = 0.8f;
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                linearAcceleration[0] = event.values[0] - gravity[0];
                linearAcceleration[1] = event.values[1] - gravity[1];
                linearAcceleration[2] = event.values[2] - gravity[2];

                String value = "加速度传感器:\nx=" + x + "\ny=" + y + "\nz=" + z + "\n\nx=" + linearAcceleration[0] + "\ny=" + linearAcceleration[1] + "\nz=" + linearAcceleration[2];
                sensorAccelerometer.setText(value);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        orientationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float azimuth_angle = event.values[0];
                float pitch_angle = event.values[1];
                float roll_angle = event.values[2];
                String value = "方向传感器:\nazimuth_angle=" + azimuth_angle
                        + "\npitch_angle=" + pitch_angle + "\nroll_angle=" + roll_angle;
                sensorOrientation.setText(value);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        proximityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float distance = event.values[0];
                String value = "距离传感器:\ndistance=" + distance;
                sensorProximity.setText(value);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        //光线传感器
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //SensorManager.SENSOR_DELAY_NORMAL = 200000微秒
        //SensorManager.SENSOR_DELAY_UI = 60000微秒
        //SensorManager.SENSOR_DELAY_GAME = 20000微秒
        //SensorManager.SENSOR_DELAY_FASTEST = 0微秒

        //加速度传感器
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //方向传感器
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(orientationListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //距离传感器
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //解除
        if (accelerometerListener != null)
            sensorManager.unregisterListener(accelerometerListener);
        if (orientationListener != null)
            sensorManager.unregisterListener(orientationListener);
        if (lightListener != null)
            sensorManager.unregisterListener(lightListener);
        if (proximityListener != null)
            sensorManager.unregisterListener(proximityListener);
    }
}
