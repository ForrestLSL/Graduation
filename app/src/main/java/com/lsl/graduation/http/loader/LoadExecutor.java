package com.lsl.graduation.http.loader;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.lsl.graduation.http.bean.BaseBean;
import com.lsl.graduation.http.callback.NetLoadCallable;
import com.lsl.graduation.http.context.LoadContext;
import com.lsl.graduation.http.loadlistener.LoadListener;
import com.lsl.graduation.utils.MLog;


/**
 * 执行线程任务的类。 现采用AsyncTask类进行线程调用，内部实现线程池以及队列管理
 * 
 * @author jialg
 * 
 * 
 *         需要补充的功能:1.线程任务的排重(已完成) 2.自定义线程的执行顺序(已完成) 3.线程取消(已完成) 4.自定义的失败重试(有需要可以添加)
 */

public class LoadExecutor {

	private static LoadExecutor executer = null;

	public static AtomicInteger PRIORITY = new AtomicInteger(1);

	private static final int CORE_POOL_SIZE = 3;
	private static final int MAXIMUM_POOL_SIZE = 5;
	private static final int KEEP_ALIVE = 1;

	/**
	 * Callback id for success computation of handler
	 */
	public static final int MESSAGE_COMPLETE = 0x303;

	/**
	 * Callback id for failed computation of handler
	 */
	public static final int MESSAGE_FAILED = 0x304;

	/**
	 * name of loader which hires this loader executor to work
	 */
	private String loaderName = "default loader";
	
	private WatchDogThread dogThread = null;

	public static LoadExecutor getInstance() {
		if (executer == null) {
			executer = new LoadExecutor();
		}
		return executer;
	}

	private LoadExecutor() {
		if (dogThread != null) {
			dogThread.quit();
		}
//		dogThread = new WatchDogThread();
//		dogThread.start();

	}

	/**
	 * 自定义的线程工厂
	 */
	private final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(final Runnable r) {
			Thread t = new Thread(r, loaderName + " LoaderExecutor #"
					+ mCount.getAndIncrement());
			t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@SuppressWarnings("rawtypes")
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					Log.e("Graduation", thread.getName()
							+ " uncaughtException called!!!");
					removeCallback(((HQFutureTask) r).context);
				}
			});
			return t;
		}
	};

	/**
	 * 排除重复任务的集合
	 */
	@SuppressWarnings("rawtypes")
	private static HashMap<String, HashSet<HQFutureTask>> queueMap = new HashMap<String, HashSet<HQFutureTask>>();

	/**
	 * 任务队列
	 */
	private final BlockingQueue<Runnable> poolWorkQueue = new PriorityBlockingQueue<Runnable>(
			128, new PriorityComparator());

	@SuppressWarnings("rawtypes")
	private class PriorityComparator<T extends HQFutureTask> implements
			Comparator<T> {

		@Override
		public int compare(HQFutureTask lhs, HQFutureTask rhs) {
			return rhs.priority - lhs.priority;
		}
	}

	/**
	 * 自定义的线程池
	 */
	public final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			poolWorkQueue, sThreadFactory, new RejectedExecutionHandler() {

				@SuppressWarnings("rawtypes")
				@Override
				public void rejectedExecution(Runnable r,
						ThreadPoolExecutor executor) {
					Log.e("Sdebug", "rejectedExecution called");
					if (!executor.isShutdown()) {
						try {
							HQFutureTask task = (HQFutureTask) executor
									.getQueue().poll();
							removeCallback(task.context);
							removeCallback(((HQFutureTask) r).context);
						} catch (Exception e) {
							Log.w("Graduation", e);
							return;
						}
						executor.execute(r);
					}
				}

			});

	@SuppressWarnings("rawtypes")
	private void removeCallback(LoadContext context) {
		String param = context.getParamsString();
		if (param != null) {
			synchronized (queueMap) {
				HashSet set = queueMap.get(param);
				set.remove(context);
				if (set.isEmpty()) {
					queueMap.remove(param);
				}
			}
		}
	}

	/**
	 * 带有优先级的FutureTask，用来自定义任务执行顺序
	 * @author jialg
	 *
	 * @param <T>
	 */
	private class HQFutureTask<T> extends FutureTask<T> {

		public int priority = PRIORITY.get();
		public long initTime;
		@SuppressWarnings("rawtypes")
		public LoadContext context;

		public HQFutureTask(Callable<T> callable) {
			super(callable);
		}

		public HQFutureTask(Runnable runnable, T result) {
			super(runnable, result);
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return super.cancel(mayInterruptIfRunning);
		}

	}

	/**
	 * 执行任务
	 * 
	 * @param
	 */
	public <Param, Target, Result> void execute(final LoadContext<Result> context) {
		if (TextUtils.isEmpty(context.getUrl())||context.isCanceled()) {
			return;
		}
		String param = context.getParamsString();
		HQFutureTask<Result> task = new HQFutureTask<Result>(new NetLoadCallable<Result>(context)) {
			@SuppressWarnings("unchecked")
			@Override
			protected void done() {
				super.done();
				Message message = Message.obtain();
				message.obj = context;
				try {
					Result result = get();
					if (result != null) {
						if(result instanceof BaseBean){
							if(((BaseBean)result).isSuccess()){
								message.what = MESSAGE_COMPLETE;
							}else{
								message.what = MESSAGE_FAILED;
							}
						}else{
							message.what = MESSAGE_COMPLETE;
						}
					} else{
						message.what = MESSAGE_FAILED;
					}
				} catch (CancellationException e) {
					// 取消
					Log.w("Graduation", "cancle     ", e);
				} catch (Exception e) {
					context.setException(e);
					if(context.getFlag() == LoadContext.FLAG_HTTP_FIRST&&context.getType() == LoadContext.TYPE_HTTP){
						context.setType(LoadContext.TYPE_CACHE);
						LoadDispatcher.getInstance().startLoading(context);
						return;
					}
					// 捕捉说有异常，视为失败
					message.what = MESSAGE_FAILED;
					context.setException(e);
					Log.w("Graduation", e);
				} finally {
					HQHandler.sendMessage(message);
				}
			}
		};
		task.priority = PRIORITY.getAndIncrement();
		task.initTime = System.currentTimeMillis();
		task.context = context;
		if(context.getMethod() == LoadContext.HTTP_LOAD_METHOD.HTTP_METHOD_POST){
			THREAD_POOL_EXECUTOR.execute(task);
		}else if (queueMap.get(param) != null) {
			addQueue(param, task);
			MLog.d( "queue update");
			return;
		}else {
			addQueue(param, task);
			MLog.d( "queue init");
			THREAD_POOL_EXECUTOR.execute(task);
		}
	}

	/**
	 * 任务执行完成的回调，可以调用LoadContext的loadComplete和loadFail。
	 */
	public final static Handler HQHandler = new Handler(Looper.getMainLooper()) {

		public void handleMessage(Message msg) {
			LoadContext context = (LoadContext) msg.obj;
			LoadListener target = null;
			if (null == context) {
				return;
			} else {
				target = context.getListener();
			}
			String param = context.getParamsString();
			HashSet<HQFutureTask> set = null;
			synchronized (queueMap) {
				set = queueMap.remove(param);
			}
			if(target == null){
				return;
			}
			switch (msg.what) {
			case MESSAGE_COMPLETE:
					target.loadComplete(context);
					if(set != null){
						for (HQFutureTask task : set) {
							if(task.context != context&&task.context.getListener()!=null){
								task.context.getListener().loadComplete(context);
							}
						}
					}
				break;
			case MESSAGE_FAILED:
				target.loadFail(context);
				if(set != null){
					for (HQFutureTask task : set) {
						if(task.context != context&&task.context.getListener()!=null){
							task.context.getListener().loadFail(context);
						}
					}
				}
				break;
			}
		};
	};

	/**
	 * 将任务加入排重队列，可以再不重复执行任务的情况下，由相同的任务进行回调
	 * @param param
	 * @param task
	 */
	@SuppressWarnings("rawtypes")
	private void addQueue(String param, HQFutureTask task) {
		synchronized (queueMap) {
			HashSet<HQFutureTask> taskMap = queueMap.get(param);
			if (taskMap == null) {
				taskMap = new HashSet<HQFutureTask>();
				taskMap.add(task);
				queueMap.put(param, taskMap);
			} else {
				taskMap.add(task);
			}
		}
//		synchronized (dogThread) {
//			dogThread.notify();
//		}
	}

	/**
	 * 看门狗线程，取消过时的请求，保持请求线程的通畅
	 * @author jialg
	 *
	 */
	public class WatchDogThread extends Thread {

		private boolean mQuit = false;
		
		@SuppressWarnings("rawtypes")
		private LoadContext mContext;
		
		@SuppressWarnings("rawtypes")
		private HQFutureTask mTask;
		
		public void quit() {
			mQuit = true;
			interrupt();
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			setName("dogThread");
			while (true) {
				try {
					MLog.d( "dog run");
					synchronized (queueMap) {
						Iterator<String> params = queueMap.keySet().iterator();
						String tag = null;
						while (params.hasNext()) {
							tag = params.next();
							HashSet<HQFutureTask> mSet = queueMap.get(tag);
							Iterator<HQFutureTask> iterator = mSet.iterator();
							boolean isRemoved = false;
							while(iterator.hasNext()){
								mTask = iterator.next();
								if ((System.currentTimeMillis() - mTask.initTime)> 15000) {
									mContext = mTask.context;
									isRemoved = true;
									Message message = Message.obtain();
									message.what = MESSAGE_FAILED;
									message.obj = mContext;
									mContext.setException(new CancellationException("task cancel by time out"));
									mTask.cancel(true);
									iterator.remove();
									MLog.d("dog catch");
									HQHandler.sendMessage(message);
									continue;
								}
							}
							if(isRemoved && !mSet.isEmpty()){
								mTask = mSet.iterator().next();
								MLog.d("dog push");
								mTask.initTime = System.currentTimeMillis();
								THREAD_POOL_EXECUTOR.execute(mTask);
							}else if(mSet.isEmpty()){
								params.remove();
							}
						}
					}
					if(queueMap.size() == 0){
						synchronized (this) {
							wait();								
						}
					}else{
						sleep(15000);
					}
				} catch (InterruptedException e) {
					// We may have been interrupted because it was time to quit.
					if (mQuit) {
						return;
					}
					continue;
				}
			}
		}
	}
	@SuppressWarnings("rawtypes")
	public static void complete(LoadContext context) {
		Message message = Message.obtain();
		message.what = LoadExecutor.MESSAGE_COMPLETE;
		message.obj = context;
		HQHandler.sendMessage(message);
	}
	
	@SuppressWarnings("rawtypes")
	public static void fail(LoadContext context) {
		Message message = Message.obtain();
		message.what = LoadExecutor.MESSAGE_FAILED;
		message.obj = context;
		HQHandler.sendMessage(message);
	}
}
