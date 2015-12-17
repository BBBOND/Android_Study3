package com.kim.asyncload;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by 伟阳 on 2015/12/12.
 */
public class CallbackImpl implements AsyncImageLoader.ImageCallback {
    private ImageView imageView;

    public CallbackImpl(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void imageLoaded(Drawable imageDrawable) {
        imageView.setImageDrawable(imageDrawable);
    }
}
