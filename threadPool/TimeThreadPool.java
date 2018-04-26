package com.xh.threadPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * threadPool com.xh.threadPool 2018 2018-4-26 上午11:58:32 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class TimeThreadPool {
	private final static String TAG = "TimeThreadPool";
	private Timer mTimer;
	private boolean isShutdown = false;
	private List<Task> mTasks;
	private List<Task> mRepeatTasks;

	public TimeThreadPool() {
		// TODO Auto-generated constructor stub
		mTimer = new Timer(TAG);
		mTasks = new ArrayList<Task>();
		mRepeatTasks = new ArrayList<Task>();
	}

	private boolean isSubmit(Runnable runnable) {
		if (isShutdown || runnable == null)
			return false;
		Task task = new Task(runnable);
		synchronized (mTasks) {
			if (mTasks.indexOf(task) >= 0)
				return false;
		}
		synchronized (mRepeatTasks) {
			if (mRepeatTasks.indexOf(task) >= 0)
				return false;
		}
		return true;
	}

	/**
	 * 
	 * 2018 2018-4-26 下午12:30:32 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param delay
	 *            延迟时间单位毫秒 void
	 */
	public void submit(Runnable command, long delay) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		mTimer.schedule(task, delay);
		synchronized (mTasks) {
			mTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午2:30:49 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务 void
	 */
	public void submit(Runnable command) {
		submit(command, new Date());
	}

	/**
	 * 
	 * 2018 2018-4-26 下午12:32:06 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param time
	 *            任务执行时间 void
	 */
	public void submit(Runnable command, Date time) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		mTimer.schedule(task, time);
		synchronized (mTasks) {
			mTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午12:33:39 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param delay
	 *            延迟时间单位毫秒
	 * @param period
	 *            周期单位毫秒 void
	 */
	public void submit(Runnable command, long delay, long period) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		mTimer.schedule(task, delay, period);
		synchronized (mRepeatTasks) {
			mRepeatTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午2:36:50 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param delay
	 *            延迟时间单位毫秒
	 * @param period
	 *            周期单位毫秒
	 * @param num
	 *            执行次数 void
	 */
	public void submit(Runnable command, long delay, long period, long num) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		task.time = num;
		mTimer.schedule(task, delay, period);
		synchronized (mRepeatTasks) {
			mRepeatTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午12:34:29 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param time
	 *            执行时间
	 * @param period
	 *            周期单位毫秒 void
	 */
	public void submit(Runnable command, Date time, long period) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		mTimer.schedule(task, time, period);
		synchronized (mRepeatTasks) {
			mRepeatTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午2:38:10 annotation：提交任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param command
	 *            任务
	 * @param time
	 *            执行时间
	 * @param period
	 *            周期单位毫秒
	 * @param num
	 *            执行次数 void
	 */
	public void submit(Runnable command, Date time, long period, long num) {
		if (!isSubmit(command))
			return;
		Task task = new Task(command);
		task.time = num;
		mTimer.schedule(task, time, period);
		synchronized (mRepeatTasks) {
			mRepeatTasks.add(task);
		}
	}

	/**
	 * 
	 * 2018 2018-4-26 下午2:25:03 annotation：移除任务 author：liuhuiliang email
	 * ：825378291@qq.com
	 * 
	 * @param runnable
	 *            需要移除的任务
	 * @return boolean
	 */
	public boolean remove(Runnable runnable) {
		Task task = new Task(runnable);
		synchronized (mTasks) {
			int index = mTasks.indexOf(task);
			if (index >= 0) {
				mTasks.get(index).cancel();
				return true;
			}
		}
		synchronized (mRepeatTasks) {
			int index = mRepeatTasks.indexOf(task);
			if (index >= 0) {
				mRepeatTasks.get(index).cancel();
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 2018 2018-4-26 下午2:25:31 annotation：移除任务 author：liuhuiliang email
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
		List<Runnable> mList = new ArrayList<Runnable>();
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
	 * 2018 2018-4-26 下午12:03:02 annotation：停止任务 author：liuhuiliang email
	 * ：825378291@qq.com void
	 */
	public void shutdownNow() {
		isShutdown = true;
		mTimer.cancel();
	}

	public void shutdown() {
		shutdownNow();
	}

	private class Task extends TimerTask {
		private Runnable mRunnable;
		private long time = -1;
		private long runTime = 1;

		public Task(Runnable runnable) {
			// TODO Auto-generated constructor stub
			mRunnable = runnable;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mRunnable.run();
			synchronized (mTasks) {
				int index = mTasks.indexOf(this);
				if (index >= 0)
					mTasks.remove(index);
			}
			runTime++;
			if (time > 0 && runTime > time)
				if (cancel()) {
					synchronized (mRepeatTasks) {
						int index = mRepeatTasks.indexOf(this);
						if (index >= 0)
							mRepeatTasks.remove(index);
					}
				}
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return mRunnable.hashCode();
		}

		@Override
		public boolean equals(Object arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null || arg0.getClass() != Task.class)
				return false;
			Task task = (Task) arg0;
			return mRunnable == task.mRunnable;
		}
	}
}
