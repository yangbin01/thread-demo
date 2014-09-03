package com.mumu.thread.made;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定制优先级executor
 * @author Administrator
 *
 */
public class MyPriorityExecutor {
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 1000, 
				TimeUnit.SECONDS, 
				new PriorityBlockingQueue<Runnable>(100));
		
		for(int i=0; i<=4;i++){
			Runnable task = new MyPriorityTask(i, "thread["+i+"]");
			executor.execute(task);
		}
		Thread.sleep(1000);
		for(int i=5; i<=8;i++){
			Runnable task = new MyPriorityTask(i, "thread["+i+"]");
			executor.execute(task);
		}
		
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("Main: End of the program");
	}
}

class MyPriorityTask implements Runnable, Comparable<MyPriorityTask>{
	
	private int priority;
	private String name;
	public MyPriorityTask(int priority, String name){
		this.priority = priority;
		this.name = name;
	}
	public int compareTo(MyPriorityTask o) {
		if (o.getPriority() > this.priority){
			return 1;
		}
		else if(o.getPriority() < this.priority){
			return -1;
		}
		return 0;
	}

	public void run() {
		System.out.println("my task name: " +getName()+", priority: "+getPriority());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
 