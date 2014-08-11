package com.mumu.thread.utils;

import java.util.List;

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
	
	public static void handerResult(List<Long> records) {
		long sum = 0L;
		for (Long record : records) {
			sum += record;
		}
		System.out.println("平均 ：" + sum / records.size() + " 毫秒");
	}
}
