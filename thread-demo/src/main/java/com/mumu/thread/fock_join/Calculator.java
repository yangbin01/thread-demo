package com.mumu.thread.fock_join;
/**
 * fork-join 实现 1+.....+100
 */
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class Calculator extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 100;
	private int begin;
	private int end;

	public Calculator(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		if ((end - begin) < THRESHOLD) {
			for (int i = begin; i < end; i++) {
				sum += i;
			}
		} else {
			int middle = (begin + end) / 2;
			Calculator left = new Calculator(begin, middle);
			Calculator right = new Calculator(middle, end);
			left.fork();
			right.fork();
			sum = left.join() + right.join();
		}
		return sum; 
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ForkJoinPool pool = new ForkJoinPool();
		Calculator c = new Calculator(0, 101);
		Future<Integer> result = pool.submit(c);
		System.out.println(result.get());
	}
}
