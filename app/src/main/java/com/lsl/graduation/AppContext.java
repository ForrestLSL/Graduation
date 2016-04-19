package com.lsl.graduation;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.lsl.graduation.utils.DeviceUtils;
import com.lsl.graduation.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Forrest on 16/4/5.
 */
public class AppContext extends Application {
    public static int deviceWidth;
    private static AppContext instance;
    public static AppContext getInstance(){
        return instance;
    }
    private SQLHelper sqlHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;// get the context
        deviceWidth= DeviceUtils.getDeviceWidth(instance);//get the device width
        Configs.Init(this);//create the cache file
        initImageLoader(this);
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
    public SQLHelper getSqlHelper(){
        if (sqlHelper==null){
            sqlHelper=new SQLHelper(instance);
        }
        return sqlHelper;
    }
    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        String filePath =
                "/Android/data/"+context.getPackageName() + "/cache/";//Environment.getExternalStorageDirectory() +

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, filePath);// 获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                // .memoryCacheExtraOptions(480, 800) // max width, max
                // height，即保存的每个缓存文件的最大长宽
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG,
                // 75, null) // Can slow ImageLoader, use it carefully
                // (Better don't use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024
                        // * 1024)) // You can pass your own memory cache
                        // implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                        // /.discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
                        // 加密
                        // .discCacheFileNameGenerator(new
                        // HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .discCacheFileCount(100) //缓存的File数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 *
                        1000, 30 * 1000)) // connectTimeout (5 s),
                        // readTimeout(30)// 超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

}
