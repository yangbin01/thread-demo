/**
 * 32位 win7 
 * 2 Intel(R) Core(TM) i3 cpu
 * 
 * 只put操作 hashtable性能高
 * 1/2 put， 1/2 get ConcurrentHashmap 性能高
 */
package com.mumu.thread.map;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mumu.thread.utils.ThreadUtils;

public class MapDemo {

	public static void main(String[] args) throws InterruptedException {
		test(32, 40000, 5);
	}
	private static void test(int threadnum, int cycle, int backNums)
			throws InterruptedException {
		System.out.println("-----------------------------------------");
		System.out.println("线程数量： " + threadnum + " || " + "循环次数：" + cycle
				+ " || " + "用例数量：" + backNums);
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new HashTableObj(new Hashtable<String, String>()));
		System.out.println("-----------------------------------------");
		testBlock(threadnum, cycle, backNums, new CurrentHashMapObj(new ConcurrentHashMap<String, String>()));
		System.out.println("-----------------------------------------");
	}

	private static void testBlock(int threadnum, int cycle, int backNums,
			BlockOjb blockobj) throws InterruptedException {
		
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
class BlockThread extends Thread{
	private int cycle;
	private BlockOjb blockobj;
	public BlockThread(int cycle, BlockOjb blockobj){
		this.blockobj = blockobj;
		this.cycle = cycle;
	}
	@Override
	public void run() {
		for(int i=0; i<cycle; i++){
			if(i%2 != 0){
			blockobj.set(i+"", i+"");
			}else{
				blockobj.get((i-1)+"");
			}
		}
	}
}
abstract class BlockOjb{
	abstract String get(String key);
	abstract void set(String key, String value);
	abstract String getName();
}
class HashTableObj extends BlockOjb{
	
	private Map<String, String> map;
	public HashTableObj(Map<String, String> map){
		this.map = map;
	}
	String get(String key) {
		return map.get(key);
	}
	void set(String key, String value) {
		map.put(key, value);
	}
	@Override
	String getName() {
		return "HashTable";
	}
}
class CurrentHashMapObj extends BlockOjb{
	private Map<String, String> map;
	public CurrentHashMapObj(Map<String, String> map){
		this.map = map;
	}
	String get(String key) {
		return map.get(key);
	}
	void set(String key, String value) {
		map.put(key, value);
	}
	@Override
	String getName() {
		return "ConcurrentHashMap";
	}
}
