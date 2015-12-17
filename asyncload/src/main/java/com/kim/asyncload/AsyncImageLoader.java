package com.kim.asyncload;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 伟阳 on 2015/12/12.
 */
public class AsyncImageLoader {
    private Map<String, SoftReference<Drawable>> imageCache = new HashMap<>();

    public Drawable loadDrawable(final String imageUrl, final ImageCallback callback) {
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            if (softReference.get() != null) {
                return softReference.get();
            }
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                callback.imageLoaded((Drawable) msg.obj);
            }
        };
        new Thread() {
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    private Drawable loadImageFromUrl(String imageUrl) {
        try {
            return Drawable.createFromStream(new URL(imageUrl).openStream(), "src");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ImageCallback {
        void imageLoaded(Drawable imageDrawable);
    }
}
