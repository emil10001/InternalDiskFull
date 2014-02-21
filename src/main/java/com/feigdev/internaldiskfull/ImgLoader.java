package com.feigdev.internaldiskfull;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ejf3 on 11/5/13.
 */
public class ImgLoader {
    private static final String TAG = "ImgLoader";

    public static Bitmap generatePic(Context context) {
        Bitmap bmp = null;
        try {
            Resources res = context.getResources();
            bmp = BitmapFactory.decodeResource(res, R.drawable.test, null);
            Log.d(TAG, "generated image");

        } catch (OutOfMemoryError e) {
            Log.w(TAG, "getPic OutOfMemoryError =(");
            return null;
        } catch (Exception e) {
            Log.w(TAG, "getPic blew up =(");
            return null;
        }
        return bmp;

    }

}
