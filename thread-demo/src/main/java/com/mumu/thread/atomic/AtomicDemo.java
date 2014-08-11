package com.mumu.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicDemo {

	public static void main(String[] args) throws Exception {
		test(50, 1000000);
	}
	private static void test(int threadnum, int cycle) throws InterruptedException{
		testAtomic(threadnum, cycle);
		testSync(threadnum, cycle);
		testLock(threadnum, cycle);
	}
	private static void testAtomic(int threadnum, int cycle) throws InterruptedException {
		AtomicInteger blockObj = new AtomicInteger();
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new AtomicThread(blockObj, cycle);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing AtomicInteger");
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
		
	}

	private static void testSync(int threadnum, int cycle) throws InterruptedException {
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		int num = 0;
		Object obj = new Object();
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new SyncThread(num, cycle, obj);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing syncronized");
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
		
	}

	private static void testLock(int threadnum, int cycle) throws InterruptedException {
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		int num = 0;
		Lock lock = new ReentrantLock();
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new LockThread(num, cycle, lock);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing Lock");
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
		
	}
}

class SyncThread extends Thread {

	private int num;
	private Object lock;
	private int cycle;

	public SyncThread(int num, int cycle, Object lock) {
		this.num = num;
		this.lock = lock;
		this.cycle = cycle;
	}

	@Override
	public void run() {
		for (int i = 0; i < cycle; i++) {
			synchronized (lock) {
				num++;
			}
		}
	}
}

class LockThread extends Thread {

	private int num;
	private Lock lock;
	private int cycle;

	public LockThread(int num, int cycle, Lock lock) {
		this.num = num;
		this.lock = lock;
		this.cycle = cycle;
	}

	public void run() {
		for (int i = 0; i < cycle; i++) {
			lock.lock();
			num++;
			lock.unlock();
		}
	}
}

class AtomicThread extends Thread {
	private AtomicInteger counter;
	private int recyle;

	public AtomicThread(AtomicInteger counter, int recyle) {
		this.counter = counter;
		this.recyle = recyle;
	}

	public void run() {
		for (int i = 0; i < recyle; i++) {
			counter.incrementAndGet();
		}
	}
}
