package com.lsl.graduation.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Forrest on 16/4/7.
 */
public class DeviceUtils {
    public static final int DEVICE_SCALE = 640;

    /**
     *  get the device width
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context){
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        return  metrics.widthPixels;
    }
    /**
     *  get the device height
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context){
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        return  metrics.heightPixels;
    }
}
