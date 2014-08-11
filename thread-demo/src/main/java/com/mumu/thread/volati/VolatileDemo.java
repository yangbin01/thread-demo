package com.mumu.thread.volati;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mumu.threead.utils.ThreadUtils;

public class VolatileDemo {

	public static void main(String[] args) throws InterruptedException {
		// 100：线程数量，100000： 循环次数, 5：5组测试
		test(20, 1000000, 10);
	}

	private static void test(int threadnum, int cycle, int backNums)
			throws InterruptedException {
		System.out.println("-----------------------------------------");
		System.out.println("线程数量： " + threadnum + " || " + "循环次数：" + cycle
				+" || " + "用例数量：" + backNums);
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new VolatileObj());
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new LockObj());
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new SyncObj());
		System.out.println("-----------------------------------------");
	}

	private static void testBlock(int threadnum, int cycle, int backNums,
			BlockObj blockobj) throws InterruptedException {
		List<Long> records = new ArrayList<Long>();
		System.out.println(blockobj.getName()+":");
		for (int i = 0; i < backNums; i++) {
			Thread[] threads = new Thread[threadnum];
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new BlockThread(cycle, blockobj);
			}
			long useTime = ThreadUtils.executeThreads(threads, cycle);
			records.add(useTime);
			System.out.println("用例"+ (i+1) + "：" +useTime + " 毫秒");
		}
		handerResult(records);
	}

	private static void handerResult(List<Long> records) {
		long sum = 0L;
		for (Long record : records){
			sum += record;
		}
		System.out.println("平均 ：" + sum/records.size() +" 毫秒");
	}

}

/**
 * 需要同步的竞争资源
 * 
 * @author yangbin
 *
 */
abstract class BlockObj {

	abstract int getNum();

	abstract void setNum(int num);

	abstract String getName();

}

/**
 * synchronized obj
 * 
 * @author yangbin
 *
 */
class SyncObj extends BlockObj {
	private int num;

	public synchronized int getNum() {
		return num;
	}

	public synchronized void setNum(int num) {
		this.num = num;
	}

	String getName() {
		return "synchronized";
	}
}

/**
 * JDK5.0 Lock obj
 * 
 * @author yangbin
 *
 */
class LockObj extends BlockObj {
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

	@Override
	String getName() {
		return "Lock";
	}
}

/**
 * volatile obj
 * 
 * @author yangbin
 *
 */
class VolatileObj extends BlockObj {
	private volatile int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	String getName() {
		return "volatile";
	}
}

/**
 * 执行线程
 * 
 * @author yangbin
 *
 */
class BlockThread extends Thread {
	private int cycle;
	private BlockObj obj;

	public BlockThread(int cycle, BlockObj obj) {
		this.obj = obj;
		this.cycle = cycle;
	}

	@Override
	public void run() {
		// 奇数get 偶数set
		for (int i = 0; i < cycle; i++) {
			if (cycle % 2 != 0) {
				obj.getNum();
			} else {
				obj.setNum(cycle);
			}
		}
	}
}
