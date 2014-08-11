package com.mumu.thread.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mumu.thread.utils.ThreadUtils;


public class AtomicDemo {

	public static void main(String[] args) throws Exception {
		// 2：线程数， 1000000：循环次数, 5: 用例数量
		test(20, 100000, 5);
	}

	private static void test(int threadnum, int cycle, int backNums)
			throws InterruptedException {
		System.out.println("-----------------------------------------");
		System.out.println("线程数量： " + threadnum + " || " + "循环次数：" + cycle
				+ " || " + "用例数量：" + backNums);
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new AtomicObj(new AtomicInteger()));
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new LockObj(new ReentrantLock()));
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new SyncObj());
		System.out.println("-----------------------------------------");
	}
	private static void testBlock(int threadnum, int cycle, int backNums, BlockObj blockobj) throws InterruptedException{
		
		List<Long> records = new ArrayList<Long>();
		System.out.println(blockobj.getName() + ":");
		
		for (int i = 0; i < backNums; i++) {
			Thread[] threads = new Thread[threadnum];
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new BlockThread(cycle, blockobj);
			}
			long useTime = ThreadUtils.executeThreads(threads, cycle);
			records.add(useTime);
			System.out.println("用例" + (i + 1) + "：" + useTime + " 毫秒");
		}
		ThreadUtils.handerResult(records);
	}
}

abstract class BlockObj {

	abstract void add();
	abstract String getName();
}

class SyncObj extends BlockObj {
	private int num = 0;

	void add() {
			synchronized (this) {
				this.num++;
			}
	}
	@Override
	String getName() {
		return "syncronized";
	}
}

class LockObj extends BlockObj {
	private int num = 0;
	private Lock lock;
	
	public LockObj(Lock lock){
		this.lock = lock;
	}
	void add() {
		lock.lock();
		this.num++;
		lock.unlock();
		
	}

	@Override
	String getName() {
		return "lock";
	}
}

class AtomicObj extends BlockObj {
	private AtomicInteger counter;
	public AtomicObj(AtomicInteger counter){
		this.counter = counter;
	}
	public void add() {
		counter.incrementAndGet();
	}

	@Override
	String getName() {
		return "atomic";
	}
}

/**
 * 以下thread 执行+1操作
 * 
 * @author yangbin
 * 
 */
class BlockThread extends Thread{
	private BlockObj blockobj;
	private int cycle;
	public BlockThread(int cycle, BlockObj blockobj){
		this.blockobj = blockobj;
		this.cycle = cycle;
	}
	
	@Override
	public void run() {
		for(int i=0; i<cycle; i++){
			blockobj.add();
		}
	}
}

