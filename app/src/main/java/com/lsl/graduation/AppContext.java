package com.lsl.graduation;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lsl.graduation.utils.DeviceUtils;

/**
 * Created by Forrest on 16/4/5.
 */
public class AppContext extends Application {
    public static int deviceWidth;
    private static AppContext instance;
    public static AppContext getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;// get the context
        deviceWidth= DeviceUtils.getDeviceWidth(instance);//get the device width
        Configs.Init(this);//create the cache file
    }
    /**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
//		return ni != null && ni.isConnectedOrConnecting();
        return ni != null;
    }

}
