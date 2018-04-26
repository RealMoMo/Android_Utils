package com.xh.threadPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * threadPool com.xh.threadPool 2018 2018-4-26 上午9:38:46 instructions：线程池
 * author:liuhuiliang email:825378291@qq.com
 **/

public class ThreadPool {
	private static final String TAG = "ThreadPool";
	private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>(
			128);// 队列
	protected static final int CPU_COUNT = Runtime.getRuntime()
			.availableProcessors();// 系统cpu数量
	protected static final int CPRE_POOL_SIZE = CPU_COUNT + 1;// 存活线程默认值
	protected static final int MAX_POOL_SIZE = (CPU_COUNT << 1) + 1;// 最大线程数
	private ThreadPoolExecutor mThreadPoolExecutor;// 没有延迟的线程池
	protected static final long SECOND = 1000;// 秒
	protected static final long MINUTE = 60 * SECOND;// 分钟
	protected static final long HOUR = 60 * MINUTE;// 小时
	protected static final long DAY = 24 * HOUR;// 天
	protected static final long KEEP_ALIVE_TIME = 10 * SECOND;// 默认存活时间
	protected static final Factory FACTORY = new Factory();
	protected static final ExecutionHandler EXECUTION_HANDLER = new ExecutionHandler();
	private boolean isShutdown = false;// 是否停止

	/**
	 * 
	 * 2018 2018-4-26 上午9:56:37 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 * @param unit
	 *            参数时间的单位
	 * @param workQueue
	 *            等待任务的队列容器
	 * @param factory
	 *            创建新线程的工厂
	 * @param handler
	 *            由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue,
			ThreadFactory factory, RejectedExecutionHandler handler) {
		mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, unit, workQueue, factory, handler);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:04:47 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 * @param unit
	 *            参数的时间单位
	 * @param workQueue
	 *            等待任务的队列容器
	 * @param factory
	 *            创建新线程的工厂
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue,
			ThreadFactory factory) {
		this(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue,
				factory, EXECUTION_HANDLER);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:06:22 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 * @param unit
	 *            参数的时间单位
	 * @param workQueue
	 *            等待任务的队列容器
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		this(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, FACTORY);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:08:05 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 * @param workQueue
	 *            等待任务的队列容器
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime,
			BlockingQueue<Runnable> workQueue) {
		this(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
				workQueue);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:09:38 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 * @param unit
	 *            参数时间的单位
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime,
			TimeUnit unit) {
		this(corePoolSize, maxPoolSize, keepAliveTime, unit, WORK_QUEUE);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:10:44 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime) {
		this(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:11:25 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param maxPoolSize
	 *            池中允许的最大线程数
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize) {
		this(corePoolSize, maxPoolSize, KEEP_ALIVE_TIME);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:12:22 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 */
	public ThreadPool(int corePoolSize, long keepAliveTime) {
		this(corePoolSize, MAX_POOL_SIZE, keepAliveTime);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:13:07 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中所保存的线程数，包括空线程
	 */
	public ThreadPool(int corePoolSize) {
		this(corePoolSize, MAX_POOL_SIZE);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:13:46 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param keepAliveTime
	 *            当线程数大于corePoolSize时，空线程等待任务的最长时间
	 */
	public ThreadPool(long keepAliveTime) {
		this(CPRE_POOL_SIZE, keepAliveTime);
	}

	public ThreadPool() {
		this(CPRE_POOL_SIZE);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:15:33 annotation：是否可以执行 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param runnable
	 *            任务
	 * @return boolean
	 */
	private boolean isExecute(Runnable runnable) {
		return !(runnable == null || isShutdown);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:16:39 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param runnable
	 *            任务 void
	 */
	public void submit(Runnable runnable) {
		if (isExecute(runnable))
			mThreadPoolExecutor.execute(runnable);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:23:44 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param commands
	 *            任务组 void
	 */
	public void submit(Collection<Runnable> commands) {
		if (commands == null || commands.size() <= 0 || isShutdown)
			return;
		int size = commands.size();
		Runnable[] runnables = commands.toArray(new Runnable[size]);
		for (int i = 0; i < size; i++) {
			submit(runnables[i]);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:24:44 annotation：移除任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            需要移除的任务
	 * @return boolean
	 */
	public boolean remove(Runnable command) {
		return mThreadPoolExecutor.remove(command);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:29:38 annotation：移除任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param commands
	 *            需要移除的任务组
	 * @return Collection<Runnable>
	 */
	public Collection<Runnable> remove(Collection<Runnable> commands) {
		if (commands == null || commands.size() <= 0)
			return null;
		int size = commands.size();
		Runnable[] runnables = commands.toArray(new Runnable[size]);
		List<Runnable> mList = new ArrayList<Runnable>(size);
		for (int i = 0; i < size; i++) {
			Runnable runnable = runnables[i];
			if (remove(runnable))
				continue;
			mList.add(runnable);
		}
		return mList;
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:31:23 annotation：停止接收任务 author：liuhuiliang email
	 * ：825378291@qq.com void
	 */
	public void shutdown() {
		isShutdown = true;
		if (!mThreadPoolExecutor.isShutdown())
			mThreadPoolExecutor.shutdown();
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:32:46 annotation：停止任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @return Collection<Runnable>
	 */
	public Collection<Runnable> shutdownNow() {
		isShutdown = true;
		return mThreadPoolExecutor.shutdownNow();
	}
}
