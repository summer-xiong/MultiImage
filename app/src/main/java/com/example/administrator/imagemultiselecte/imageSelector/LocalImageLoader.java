package com.example.administrator.imagemultiselecte.imageSelector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/28.
 */
public class LocalImageLoader {
    //private ImageLruCache lc = ImageLruCache.getInstance();
    private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    private Handler mHandler = new Handler();

    public void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    //获取图片
    private Bitmap revisionImage(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 256)
                    && (options.outHeight >> i <= 256)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public void displayImage(final ImageView iv, final String thumbPath,
                             final String sourcePath, final PerformImage pf) {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
            return;
        }

        final String path;
        final boolean isThumbPath;
        if (!TextUtils.isEmpty(thumbPath)) {
            path = thumbPath;
            isThumbPath = true;
        } else if (!TextUtils.isEmpty(sourcePath)) {
            path = sourcePath;
            isThumbPath = false;
        } else {
            // iv.setImageBitmap(null);
            return;
        }
        if (imageCache.containsKey(path)) {
            SoftReference<Bitmap> reference = imageCache.get(path);
            Bitmap bmp = reference.get();
            if (bmp != null) {
                if(pf != null){
                    pf.performImage(iv, bmp, sourcePath);
                }
                iv.setImageBitmap(bmp);
                return;
            }
        }

        new Thread() {
            Bitmap thumb;

            public void run() {

                try {
                    if (isThumbPath) {
                        thumb = BitmapFactory.decodeFile(thumbPath);
                        Log.d("local", thumbPath);
                        if (thumb == null) {
                            thumb = revisionImage(sourcePath);
                        }
                    } else {
                        thumb = revisionImage(sourcePath);
                    }
                } catch (Exception e) {

                }
                if(thumb != null){
                }
                put(path, thumb);
                if(pf != null && thumb != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pf.performImage(iv, thumb, sourcePath);
                        }
                    });
                }

            }
        }.start();
    }

    public interface PerformImage {
        public void performImage(ImageView imageView, Bitmap bitmap,
                                 Object... params);
    }
}
