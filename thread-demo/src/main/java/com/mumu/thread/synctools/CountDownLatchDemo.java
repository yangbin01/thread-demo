package com.mumu.thread.synctools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
/**
 * 等待多线程完成的CountDownLatch
 * @author yangbin
 *
 */
public class CountDownLatchDemo {
	
	private static CountDownLatch count = new CountDownLatch(2);
	
	public static void main(String[] args) throws InterruptedException {
		for (int i=0;i<2;i++){
			new Thread(new Runnable() {
				
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName()+" run over");
					count.countDown();
				}
			}, "thread-"+i).start();
		}
		count.await();
		System.out.println("demo end");
	}
}
