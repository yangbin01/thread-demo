package com.mumu.thread.made;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定制executor
 * 继承ThreadPoolExecutor，覆盖ThreadPoolExecutor的一些方法完成定制化，复写方法是最后调用super
 * @author Administrator
 * 
 */
public class MyExecutor extends ThreadPoolExecutor {

	private ConcurrentHashMap<String, Date> startTimes;

	public MyExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		startTimes = new ConcurrentHashMap<String, Date>();
	}

	@Override
	public void shutdown() {
		System.out.println("MyExecutor going to shundown");
		System.out.println("MyExecutor: Executed tasks:"
				+ getCompletedTaskCount());
		System.out.println("MyExecutor: Running tasks:" + getActiveCount());
		System.out.println("MyExecutor: Pending tasks:" + getQueue().size());
		super.shutdown();
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.out.println(t.getName() + ": beginning run");
		startTimes.put(String.valueOf(r.hashCode()), new Date());
		super.beforeExecute(t, r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		System.out.println("a task finished run");
		Date date = startTimes.remove(String.valueOf(r.hashCode()));
		System.out.println("using: " + (new Date().getTime() - date.getTime()));
		super.afterExecute(r, t);
	}

	public static void main(String[] args) {
		MyExecutor executor = new MyExecutor(2, 4, 1000, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());

		for (int i = 0; i < 10; i++) {
			Runnable task = new Task();
			executor.execute(task);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		executor.shutdown();

		try {

			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

		System.out.println("Main: end of the program");
	}
}

class Task implements Runnable {

	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}