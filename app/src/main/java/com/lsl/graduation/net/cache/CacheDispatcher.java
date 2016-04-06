/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsl.graduation.net.cache;

import android.os.Process;

import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.loader.LoadExecutor;

import java.text.ParseException;
import java.util.concurrent.BlockingQueue;


/**
 * Provides a thread for performing cache triage on a queue of requests.
 * 
 * Requests added to the specified cache queue are resolved from cache. Any
 * deliverable response is posted back to the caller via a
 * {@link //ResponseDelivery}. Cache misses and responses that require refresh are
 * enqueued on the specified network queue for processing by a
 * {@link //NetworkDispatcher}.
 */
@SuppressWarnings("rawtypes")
public class CacheDispatcher extends Thread {

	/** The queue of requests coming in for triage. */
	private final BlockingQueue<LoadContext> mCacheQueue;

	/** The queue of requests going out to the network. */
	private final LoadExecutor mNetworkExecutor;

	/** The cache to read from. */
	private final Cache mCache;

	/** Used for telling us to die. */
	private volatile boolean mQuit = false;

	/**
	 * Creates a new cache triage dispatcher thread. You must call
	 * {@link #start()} in order to begin processing.
	 * 
	 * @param cacheQueue
	 *            Queue of incoming requests for triage
	 * @param //networkQueue
	 *            Queue to post requests that require network to
	 * @param cache
	 *            Cache interface to use for resolution
	 * @param //delivery
	 *            Delivery interface to use for posting responses
	 */
	public CacheDispatcher(BlockingQueue<LoadContext> cacheQueue,
			LoadExecutor NetworkExecutor, Cache cache) {
		mCacheQueue = cacheQueue;
		mNetworkExecutor = NetworkExecutor;
		mCache = cache;
	}

	/**
	 * Forces this dispatcher to quit immediately. If any requests are still in
	 * the queue, they are not guaranteed to be processed.
	 */
	public void quit() {
		mQuit = true;
		interrupt();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		// Make a blocking call to initialize the cache.
		mCache.initialize();

		while (true) {
			try {
				// Get a request from the cache triage queue, blocking until
				// at least one is available.
				final LoadContext context = mCacheQueue.take();
				try {
					if (context.isCanceled()) {
						throw new InterruptedException("task cancel by user");
						// continue;
					}
					if (context.getFlag() == LoadContext.FLAG_HTTP_ONLY) {
						mNetworkExecutor.execute(context);
						continue;
					}
					// Attempt to retrieve this item from cache.
					switch (context.getFlag()) {
					case LoadContext.FLAG_CACHE_ONLY:
						loadByCache(context);
						continue;
					case LoadContext.FLAG_HTTP_FIRST:
						if (context.getType() == LoadContext.TYPE_CACHE) {
							loadByCache(context);
						} else {
							mNetworkExecutor.execute(context);
						}
						continue;
					case LoadContext.FLAG_CACHE_FIRST:
					default:
						Cache.Entry entry = mCache.get(context.getParamsString());
						if (entry == null) {
							mNetworkExecutor.execute(context);
							continue;
						} else {
							try {
								context.setResult(context.getParser().parse(entry.data));
								context.setType(LoadContext.TYPE_CACHE);
								context.setResponseHeaders(entry.responseHeaders);
							} catch (ParseException e) {
							}
						}
						if (entry == null) {
							LoadExecutor.fail(context);
						} else {
							LoadExecutor.complete(context);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LoadExecutor.fail(context);
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

	private void loadByCache(final LoadContext context) {
		Cache.Entry entry = mCache.get(context.getParamsString());
		if (entry != null) {
			try {
				context.setResult(context.getParser().parse(entry.data));
				context.setType(LoadContext.TYPE_CACHE);
				context.setResponseHeaders(entry.responseHeaders);
			} catch (ParseException e) {
			}
		}
		context.setType(LoadContext.TYPE_CACHE);
		if (entry == null) {
			LoadExecutor.fail(context);
		} else {
			LoadExecutor.complete(context);
		}
	}

}
