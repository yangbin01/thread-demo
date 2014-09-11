package com.mumu.thread.synctools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 同步屏障CyclicBarrier
 * 
 * @author yangbin
 *
 */
public class CyclicBarrierDemo {

	private static final int COUNT = 10;
	private static CyclicBarrier barrier = new CyclicBarrier(COUNT,
			new BarrierRunnable());

	public static void main(String[] args) throws InterruptedException,
			BrokenBarrierException {
		for (int i = 0; i < COUNT; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(2000));
						System.out.println(Thread.currentThread().getName()
								+ " waiting");
						barrier.await();
						System.out.println(Thread.currentThread().getName()
								+ " go on running");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}
}

class BarrierRunnable implements Runnable {

	public void run() {
		System.out.println("BarrierRunnable run first");
	}

}