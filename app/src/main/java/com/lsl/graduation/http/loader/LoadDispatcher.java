package com.lsl.graduation.http.loader;

import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

import com.lsl.graduation.Configs;
import com.lsl.graduation.http.cache.Cache;
import com.lsl.graduation.http.cache.CacheDispatcher;
import com.lsl.graduation.http.cache.DiskBasedCache;
import com.lsl.graduation.http.context.LoadContext;
import com.lsl.graduation.utils.MLog;


public class LoadDispatcher implements Loader {
	
	private static LoadExecutor executor = null;
	private static LoadDispatcher instance;
	/** The cache triage queue. */
    @SuppressWarnings("rawtypes")
	private final LinkedBlockingQueue<LoadContext> mCacheQueue =
        new LinkedBlockingQueue<LoadContext>();
	private static Cache cache;
	
	public static LoadDispatcher getInstance() {
		if(instance == null){
			instance = new LoadDispatcher();
		}
		return instance;
	}
	
	private LoadDispatcher() {
		executor = LoadExecutor.getInstance();//new LoaderExecutor(this.getClass().getSimpleName());
		cache = new DiskBasedCache(Configs.cacheFile);
		CacheDispatcher dispatcher = new CacheDispatcher(mCacheQueue, executor, cache);
		dispatcher.start();
	}
	
	public static Cache getCacheManager(){
		return cache;
	}
	
	@Override
	public <Result> void onPreLoad(LoadContext<Result> context) {
		
	}

	@Override
	public <Result> void startLoading(LoadContext<Result> context) {
		if(context.isCanceled()){
			return;
		}
		if(context.getListener() == null){
			MLog.d( "the taget is null and has no default");
		}else{
			try {
				context.getListener().preExecut(context);
			} catch (Exception e) {
				context.setException(e);
				context.getListener().loadFail(context);
			}
		}
		mCacheQueue.add(context);
	}
	
	@Override
	public <Result> void onPostLoad(LoadContext<Result> context) {
		
	}

	@Override
	public <Result> boolean cancelLoading(LoadContext<Result> context) {
		return false;
	}

	@Override
	public void destroy(boolean now) {
		
	}
	
}
