package com.mumu.thread.synctools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
/**
 * Semaphore（信号量）控制同时访问特定资源的线程数量
 * @author yangbin
 *
 */
public class SemaphoreDemo {

	private static Semaphore sem = new Semaphore(10);
	private static final int THREAD_COUNT = 30;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(THREAD_COUNT);

	public static void main(String[] args) {
		for(int i=0; i<THREAD_COUNT; i++){
			threadPool.execute(new Runnable() {
				
				public void run() {
					try {
						sem.acquire();
						System.out.println("Semaphore length: " + sem.availablePermits());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						sem.release();
					}
				}
			});
		}
		threadPool.shutdown();
	}
}
