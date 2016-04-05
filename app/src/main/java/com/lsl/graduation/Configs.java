package com.lsl.graduation;

import android.content.Context;

import java.io.File;

/**
 * Created by Forrest on 16/4/5.
 */
public class Configs {
    public static File cacheFile;
    public static void Init(Context context){
        cacheFile = new File(context.getCacheDir()+"graduation");
//        version = getPackageVersion(context);
//        WebView view = new WebView(context);
//        userAgent = view.getSettings().getUserAgentString();
//        LoadDispatcher.getInstance();
//        PhoneManager.getInstance(context).registerSystemReceiver();
//        PhoneManager.getInstance(context).addOnNetWorkChangeListener(
//                ApnManager.getInstance(context));
//        initImageLoader(context);
    }

    /**连接超时*/
    public final static int CONNECTION_TIMEOUT = 15 * 1000;
    /** SO连接超时*/
    public final static int SO_TIMEOUT = 15 * 1000;
}
