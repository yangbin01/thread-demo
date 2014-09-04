package com.mumu.thread.made;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现ThreadFactory接口生成自定义的线程， 在一个Executor对象中使用我们的ThreadFactory
 * @author yangbin
 *
 */
public class MyThreadFactory implements ThreadFactory{
	private AtomicInteger count;
	private String name;
	
	public MyThreadFactory(String name){
		this.name = name;
		count = new AtomicInteger();
	}
	public Thread newThread(Runnable r) {
		Thread t = new MyThread(r, this.name+"--"+count.get());
		count.incrementAndGet();
		return t;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyThreadFactory fac = new MyThreadFactory("MyThreadFactory");
		Thread t = fac.newThread(new Runnable() {	
			public void run() {
				System.out.println("running.............");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		t.join();
		System.out.println("Main: Thread information.");
		System.out.println(t);
		System.out.println("Main: End of the example.");
		Thread.sleep(1000);
		//在一个Executor对象中使用我们的ThreadFactory
		System.out.println("在一个Executor对象中使用我们的ThreadFactory");
		ExecutorService server = Executors.newSingleThreadExecutor(fac);
		server.execute(new Task());
		server.shutdown();
		server.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("end");
	}
	
}

class MyThread extends Thread{
	private Date createTime;
	private Date runTime;
	private Date stopTime;
	
	public MyThread(Runnable target, String name){
		super(target, name);
		createTime = new Date();
	}
	
	@Override
	public void run() {
		setRunTime(new Date());
		super.run();
		setStopTime(new Date());
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getRunTime() {
		return runTime;
	}
	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getName()+": " + getCreateTime()+" ,running ");
		builder.append(getStopTime().getTime()-getRunTime().getTime()+ " mils");
		return builder.toString();
	}
}
