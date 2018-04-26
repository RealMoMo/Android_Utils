package com.xh.threadPool;

import java.util.concurrent.ThreadFactory;

/**
 * threadPool com.xh.threadPool 2018 2018-4-26 ÉÏÎç10:02:41 instructions£º
 * author:liuhuiliang email:825378291@qq.com
 **/

public class Factory implements ThreadFactory {
	private final static String TAG = "Factory";

	@Override
	public Thread newThread(Runnable arg0) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(arg0, TAG + System.currentTimeMillis());
		thread.setPriority(Thread.MAX_PRIORITY);
		return thread;
	}

}
