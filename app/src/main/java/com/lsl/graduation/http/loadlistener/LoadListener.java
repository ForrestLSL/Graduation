package com.lsl.graduation.http.loadlistener;


import com.lsl.graduation.http.context.LoadContext;

public interface LoadListener<Result> {
	
	/**
	 * 任务执行之前调用（主线程）
	 * @param context
	 */
	public void preExecut(LoadContext<Result> context);
	
	/**
	 * loadComplete之前调用（子线程），用于数据校验
	 * @param context
	 */
	public void postExecut(LoadContext<Result> context);
	
	/**
	 * 任务完成时调用（主线程）
	 * @param context
	 */
	public void loadComplete(LoadContext<Result> context);
	
	/**
	 * 任务失败时调用（主线程）
	 * @param context
	 */
	public void loadFail(LoadContext<Result> context);
}
