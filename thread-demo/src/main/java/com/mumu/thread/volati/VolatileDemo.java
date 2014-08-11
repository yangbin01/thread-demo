package com.mumu.thread.volati;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VolatileDemo {

	public static void main(String[] args) throws InterruptedException {
		test(100, 100000);
	}

	private static void test(int threadnum, int cycle)
			throws InterruptedException {
		testAtomic(threadnum, cycle);
		testSync(threadnum, cycle);
		testVolatile(threadnum, cycle);
	}

	private static void testVolatile(int threadnum, int cycle)
			throws InterruptedException {
		VolatileObj blockobj = new VolatileObj();
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new VolatileThread(cycle, blockobj);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing Volatile");
		System.out.println("result: "+ blockobj.getNum());
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out
				.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
	}

	private static void testSync(int threadnum, int cycle) throws InterruptedException {
		SyncObj blockobj = new SyncObj();
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new SyncrThread(cycle, blockobj);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing sync");
		System.out.println("result: "+ blockobj.getNum());
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out
				.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
	}

	private static void testAtomic(int threadnum, int cycle) throws InterruptedException {
		LockObj blockobj = new LockObj();
		long begin = System.currentTimeMillis();
		Thread[] threads = new Thread[threadnum];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new LockThread(cycle, blockobj);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println("-----------------testing lock");
		System.out.println("result: "+ blockobj.getNum());
		System.out.println("线程数量: " + threads.length);
		System.out.println("线程循环次数： " + cycle);
		System.out
				.println("用时: " + (System.currentTimeMillis() - begin) + "毫秒");
	}
}

class SyncObj{
	private int num;

	public synchronized int getNum() {
		return num;
	}

	public synchronized void setNum(int num) {
		this.num = num;
	}
}

class LockObj {
	private int num;
	private Lock lock = new ReentrantLock();

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		lock.lock();
		this.num = num;
		lock.unlock();
	}
}

class VolatileObj {
	private volatile int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}

class SyncrThread extends Thread {
	private SyncObj obj;
	private int cycle;

	public SyncrThread(int cycle,SyncObj obj) {
		this.obj = obj;
		this.cycle = cycle;
	}

	@Override
	public void run() {
		for (int i = 0; i < cycle; i++) {
			if (cycle % 2 != 0) {
				obj.getNum();
			} else {
				obj.setNum(cycle);
			}
		}
	}
}
class LockThread extends Thread {
	private LockObj obj;
	private int cycle;

	public LockThread(int cycle,LockObj obj) {
		this.obj = obj;
		this.cycle = cycle;
	}

	@Override
	public void run() {
		for (int i = 0; i < cycle; i++) {
			if (cycle % 2 != 0) {
				obj.getNum();
			} else {
				obj.setNum(cycle);
			}
		}
	}
}
class VolatileThread extends Thread {
	private VolatileObj obj;
	private int cycle;

	public VolatileThread(int cycle, VolatileObj obj) {
		this.obj = obj;
		this.cycle = cycle;
	}

	@Override
	public void run() {
		for (int i = 0; i < cycle; i++) {
			if (cycle % 2 != 0) {
				obj.getNum();
			} else {
				obj.setNum(cycle);
			}
		}
	}
}