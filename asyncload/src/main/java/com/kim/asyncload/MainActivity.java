package com.kim.asyncload;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private AsyncImageLoader loader = new AsyncImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadImage("http://a.ikafan.com/attachment/forum/month_0807/20080726_877668de4f27856575b3y1PzoiAhYveb.jpg", R.id.imageView01);
        loadImage("http://pic22.nipic.com/20120705/668573_091208280175_2.jpg", R.id.imageView02);
        loadImage("http://img.cache.pdawo.com/uploads/allimg/20130725/thfbylwyvzw.jpg", R.id.imageView03);
    }

    private void loadImage(String url, int id) {
        ImageView imageView = (ImageView) findViewById(id);
        CallbackImpl callback = new CallbackImpl(imageView);
        Drawable cacheImage = loader.loadDrawable(url, callback);
        if (cacheImage != null) {
            imageView.setImageDrawable(cacheImage);
        }
    }
}
