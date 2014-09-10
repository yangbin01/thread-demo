package com.mumu.thread.synctools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 同步屏障CyclicBarrier
 * 
 * @author yangbin
 *
 */
public class CyclicBarrierDemo {
	
	private static CyclicBarrier barrier = new CyclicBarrier(2, new BarrierRunnable());
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		new Thread(new Runnable() {
			public void run() {
				try {
					barrier.await();
					System.out.println(Thread.currentThread().getName()+" run over");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		}, "barrier-thread").start();
		barrier.await();
		System.out.println("main run over");
		
	}
}

class BarrierRunnable implements Runnable{

	public void run() {
		System.out.println("BarrierRunnable run first");
	}
	
}