package com.kim.sensor;

import android.content.Context;
import android.hardware.Sensor;
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

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        ButterKnife.bind(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        showSensorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
                String string = "";
                for (Sensor sensor : sensors) {
                    string += sensor.getName() + " " + sensor.getVersion() + "\n";
                }
                sensorList.setText(string);
            }
        });
    }
}
