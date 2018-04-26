package com.xh.threadPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * threadPool com.xh.threadPool 2018 2018-4-26 上午10:33:34 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class ScheduledThreadPool {
	private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
	private boolean isShutdown = false;

	/**
	 * 
	 * 2018 2018-4-26 上午10:36:04 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中保存的线程数，包括空线程
	 * @param threadFactory
	 *            创建新线程的工厂
	 * @param handler
	 *            由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
	 */
	public ScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		// TODO Auto-generated constructor stub
		mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
				corePoolSize, threadFactory, handler);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:41:12 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中保存的线程数，包括空线程
	 * @param threadFactory
	 *            创建新线程的工厂
	 */
	public ScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
		// TODO Auto-generated constructor stub
		this(corePoolSize, threadFactory, ThreadPool.EXECUTION_HANDLER);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:41:24 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中保存的线程数，包括空线程
	 * @param handler
	 *            由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
	 */
	public ScheduledThreadPool(int corePoolSize,
			RejectedExecutionHandler handler) {
		// TODO Auto-generated constructor stub
		this(corePoolSize, ThreadPool.FACTORY, handler);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:41:38 author：liuhuiliang email ：825378291@qq.com
	 * 
	 * @param corePoolSize
	 *            池中保存的线程数，包括空线程
	 */
	public ScheduledThreadPool(int corePoolSize) {
		// TODO Auto-generated constructor stub
		this(corePoolSize, ThreadPool.FACTORY);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:44:41 annotation：是否可执行任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param runnable
	 *            任务
	 * @return boolean
	 */
	private boolean isSchedule(Runnable runnable) {
		return !(runnable == null || isShutdown);
	}

	public ScheduledThreadPool() {
		// TODO Auto-generated constructor stub
		this(ThreadPool.CPRE_POOL_SIZE);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:46:06 annotation：延迟提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param delay
	 *            延迟时间单位为毫秒 void
	 */
	public void submit(Runnable command, long delay) {
		if (isSchedule(command))
			mScheduledThreadPoolExecutor.schedule(command, delay,
					TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:50:03 annotation：延迟提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param delay
	 *            延迟时间单位为毫秒
	 * @param period
	 *            周期单位为毫秒 void
	 */
	public void submit(Runnable command, long delay, long period) {
		if (isSchedule(command))
			mScheduledThreadPoolExecutor.scheduleAtFixedRate(command, delay,
					period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午10:51:23 annotation：移除任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            要移除的任务
	 * @return boolean
	 */
	public boolean remove(Runnable command) {
		return mScheduledThreadPoolExecutor.remove(command);
	}

	/**
	 * 
	 * 2018 2018-4-26 上午11:18:23 annotation：移除任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param commands
	 *            要移除的任务组
	 * @return Collection<Runnable>
	 */
	public Collection<Runnable> remove(Collection<Runnable> commands) {
		if (commands == null || commands.size() <= 0)
			return null;
		int size = commands.size();
		List<Runnable> mList = new ArrayList<Runnable>(size);
		Runnable[] runnables = commands.toArray(new Runnable[size]);
		for (int i = 0; i < size; i++) {
			Runnable command = runnables[i];
			if (remove(command))
				continue;
			mList.add(command);
		}
		return mList;
	}

	/**
	 * 
	 * 2018 2018-4-26 上午11:57:31 annotation：停止接收任务 author：liuhuiliang email
	 * ：825378291@qq.com void
	 */
	public void shutdown() {
		isShutdown = true;
		if (!mScheduledThreadPoolExecutor.isShutdown())
			mScheduledThreadPoolExecutor.shutdown();
	}

	/**
	 * 
	 * 2018 2018-4-26 上午11:57:47 annotation：停止任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @return Collection<Runnable>
	 */
	public Collection<Runnable> shutdownNow() {
		isShutdown = true;
		return mScheduledThreadPoolExecutor.shutdownNow();
	}
}
