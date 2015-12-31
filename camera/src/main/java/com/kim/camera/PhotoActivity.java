package com.kim.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView fullscreenContent;
    private Button take;

    private Camera camera;
    private boolean isPreview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //可以通过主题实现
        //android:windowNoTitle:true
        //android:windowFullscreen:?android:windowFullscreen
        Window window = getWindow();
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置高亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().hide();

        fullscreenContent = (SurfaceView) findViewById(R.id.fullscreen_content);
        take = (Button) findViewById(R.id.take);

        //设置分辨率
        fullscreenContent.getHolder().setFixedSize(1280, 720);
        //设置surfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
        fullscreenContent.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        fullscreenContent.getHolder().addCallback(new SurfaceCallback());

        take.setOnClickListener(this);
        fullscreenContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take:
                if (camera != null) {
                    takePhoto();
                }
                break;
            case R.id.fullscreen_content:
                if (camera != null) {
                    autoFocus();
                }
                break;
        }

    }

    private void takePhoto() {
        if (camera != null) {
            camera.takePicture(null, null, new TakePictureCallback());
        }
    }

    private void autoFocus() {
        if (camera != null)
            camera.autoFocus(null);//自动对焦
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        //在SurfaceView创建完成后调用
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                //打开摄像头
                camera = Camera.open();
                //获取屏幕
                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                //获取相机参数集
                Camera.Parameters parameters = camera.getParameters();
                //设置预览尺寸
                parameters.setPreviewSize(display.getWidth(), display.getHeight());
                //设置每秒帧数
                parameters.setPreviewFrameRate(30);
                //设置照片格式
                parameters.setPictureFormat(PixelFormat.JPEG);
                //设置照片质量
                parameters.set("jpeg-quality", 85);
                //设置照片大小
                parameters.setPictureSize(display.getWidth(), display.getHeight());
                //设置相机参数
                camera.setParameters(parameters);
                //设置预览显式的地方
                camera.setPreviewDisplay(fullscreenContent.getHolder());
                if (!isPreview) {
                    //开始预览
                    camera.startPreview();
                    isPreview = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private final class TakePictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream fileOutputStream = null;
            try {
                camera.stopPreview();
                //生成位图
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //获取文件
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", System.currentTimeMillis() + ".jpg");
                //获取文件输出流
                fileOutputStream = new FileOutputStream(file);
                //将位图保存在文件中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                //重新开始预览
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (camera != null && event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                case KeyEvent.KEYCODE_CAMERA:
                    takePhoto();
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null && isPreview) {
            //停止预览
            camera.stopPreview();
            //释放相机
            camera.release();
        }
    }
}
