package com.kim.camera;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.fullscreen_content)
    SurfaceView fullscreenContent;
    @Bind(R.id.take)
    Button take;
    @Bind(R.id.screen_touch)
    FrameLayout screenTouch;

    private MediaRecorder mediaRecorder;

    private boolean isTaking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置高亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mediaRecorder = new MediaRecorder();

        //设置分辨率
//        fullscreenContent.getHolder().setFixedSize(1280, 720);
        //设置surfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
        fullscreenContent.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        take.setOnClickListener(this);
        screenTouch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(VideoActivity.this, "内存卡不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.take:
                if (!isTaking) {
                    isTaking = true;
                    take.setText("停止");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startRecording();
                        }
                    }).start();
                } else {
                    isTaking = false;
                    take.setText("录制");
                    stopRecording();
                }
                break;
        }
    }

    //开始拍摄
    public boolean startRecording() {
        try {
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    @Override
                    public void onError(MediaRecorder mr, int what, int extra) {
                        mr.reset();
                    }
                });
            } else {
                mediaRecorder.reset();
            }
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoSize(1280, 720);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setAudioSamplingRate(16000);
            File videoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "Video" + System.currentTimeMillis() + ".mp4");
            mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
            mediaRecorder.setPreviewDisplay(fullscreenContent.getHolder().getSurface());
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(VideoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //停止拍摄
    public void stopRecording() {
        try {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
