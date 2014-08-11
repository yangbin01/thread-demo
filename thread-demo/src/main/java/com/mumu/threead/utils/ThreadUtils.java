package com.mumu.threead.utils;

public class ThreadUtils {

	public static long executeThreads(Thread[] threads, int cycle) throws InterruptedException{
		
		long begin = System.currentTimeMillis();
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}	
		return System.currentTimeMillis() - begin;
	}
}
