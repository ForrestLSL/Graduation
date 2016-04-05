package com.lsl.graduation.http.loader;


import com.lsl.graduation.http.context.LoadContext;

/**
 * Loader的设计目标是封装线程任务的调度,排序。
 *
 *            请求参数
 *            回调通知的目标,通常它应该是一个视图(View)或者Activity
 *            载入结果
 * @author jialg
 * 
 */
public interface Loader {
	
	/**
	 * 预加载回调方法，在UI线程调用
	 * 
	 * @param context
	 */
	<Result> void onPreLoad(LoadContext<Result> context);
	
	/**
	 * 发起载入的异步请求。
	 * 
	 * @param context
	 */
	<Result> void startLoading(LoadContext<Result> context);

	/**
	 * 完成加载请求
	 * 
	 * @param context
	 */
	<Result> void onPostLoad(LoadContext<Result> context);
	
	/**
	 * 取消该上下文的任务
	 * 
	 * @param context
	 */
	<Result> boolean cancelLoading(LoadContext<Result> context);

	/**
	 * 破坏Loader。终止所有异步线程和任务
	 * 
	 * @param now
	 *            指示是否希望立即破坏。(当有任务正在执行时若设置为now则应尝试中断)
	 */
	void destroy(boolean now);
}
