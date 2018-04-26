package com.xh.threadPool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * threadPool com.xh.threadPool
 * 2018 2018-4-26 上午10:01:58
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class ExecutionHandler implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable arg0, ThreadPoolExecutor arg1) {
		// TODO Auto-generated method stub
		System.out.println("超出队列");
//		arg1.execute(arg0);
	}

}
