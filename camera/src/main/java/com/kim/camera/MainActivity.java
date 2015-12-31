package com.kim.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.take_photo)
    Button takePhoto;
    @Bind(R.id.take_video)
    Button takeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        takePhoto.setOnClickListener(this);
        takeVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.take_photo:
                intent = new Intent(MainActivity.this, PhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.take_video:
                intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
