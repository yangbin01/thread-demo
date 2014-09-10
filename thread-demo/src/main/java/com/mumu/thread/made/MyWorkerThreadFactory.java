package com.mumu.thread.made;

import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 实现ThreadFactory接口生成自定义的线程给Fork/Join框架
 * 
 * @author yangbin
 *
 */
public class MyWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

	public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
		return new MyForkJoinWorkerThread(pool);
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyWorkerThreadFactory f = new MyWorkerThreadFactory();
		ForkJoinPool pool = new ForkJoinPool(4, f, null, false);
		MyRecursiveTask task = new MyRecursiveTask(1, 400);
		
		pool.execute(task);
		task.join();
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("end");
		
	}
}

class MyForkJoinWorkerThread extends ForkJoinWorkerThread {

	private static ThreadLocal<Integer> taskCounter = new ThreadLocal<Integer>();

	protected MyForkJoinWorkerThread(ForkJoinPool pool) {
		super(pool);
	}

	@Override
	protected void onStart() {
		System.out.println("MyWorkerThread " + getId()
				+ "nitializing taskcounter.");
		taskCounter.set(0);
		super.onStart();
	}

	@Override
	protected void onTermination(Throwable exception) {
		System.out.printf("MyWorkerThread %d:%d\n", getId(), taskCounter.get());
		super.onTermination(exception);
	}
	
	public void addTask(){
		int counter=taskCounter.get().intValue();
		counter++;
		taskCounter.set(counter);
	}
}

class MyRecursiveTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;
	private int start, end;

	public MyRecursiveTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		if (end-start<100){
			for(int i=start;i<end;i++){
				sum = sum+i;
			}
			MyForkJoinWorkerThread thread=(MyForkJoinWorkerThread)Thread.currentThread();
			thread.addTask();
		}
		else{
			int middle = (start + end) / 2;
			MyRecursiveTask left = new MyRecursiveTask(start, middle);
			MyRecursiveTask right = new MyRecursiveTask(middle, end);
			left.fork();
			right.fork();
			sum = left.join() + right.join();

		}
		return sum;
	}
	
}