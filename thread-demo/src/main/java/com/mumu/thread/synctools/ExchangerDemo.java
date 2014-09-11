package com.mumu.thread.synctools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {

	private static Exchanger<String> changer = new Exchanger<String>();
	private static ExecutorService server = Executors.newFixedThreadPool(2);

	public static void main(String[] args) {

		server.execute(new Runnable() {

			public void run() {
				try {
					String str = "银行流水A";
					String threadName = Thread.currentThread().getName();
					System.out.println(threadName+" set: "+str+ " and get: "+changer.exchange(str));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		server.execute(new Runnable() {

			public void run() {
				try {
					String str = "银行流水B";
					String threadName = Thread.currentThread().getName();
					System.out.println(threadName+" set: "+str+ " and get: "+changer.exchange(str));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		server.shutdown();
	}
}
