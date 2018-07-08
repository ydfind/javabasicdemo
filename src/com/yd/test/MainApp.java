package com.yd.test;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.yd.test.util.Map.MyHashMap;
import com.yd.test.util.Map.MyHashtable;
import com.yd.test.util.Map.MyMap;

public class MainApp {
	private static int tableSizeFor(int cap) {
		int MAXIMUM_CAPACITY = 1 << 30;
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		int result = (n < 0) ? 1: (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
		System.out.println(cap + "对应2的幂为：" + result);
		return result;
	}
	private static void tableSizeForTest() {
		tableSizeFor(1);
		tableSizeFor(3);
		tableSizeFor(7);
		tableSizeFor(8);
		tableSizeFor(9);
		tableSizeFor(15);
		tableSizeFor(-31);
		tableSizeFor((1 << 20) - 1);
		tableSizeFor((1 << 25) - 1);
		tableSizeFor((1 << 30) - 1);
		tableSizeFor((1 << 31) - 1);
		int n, l, res;
		n = 9;
		res = n;
		
		l = 1;
		res |= n >>> l;
		System.out.println(n + " >>> " + l + " |= " + res);
		l = 2;
		res |= n >>> l;
		System.out.println(n + " >>> " + l + " |= " + res);
		l = 4;
		res |= n >>> l;
		System.out.println(n + " >>> " + l + " |= " + res);
		l = 8;
		res |= n >>> l;
		System.out.println(n + " >>> " + l + " |= " + res);
		l = 16;
		res |= n >>> l;
		System.out.println(n + " >>> " + l + " |= " + res);
		System.out.println(" 最终结果为： " + (res + 1));
		
	}
	
	private static void remameFile() {
		String oldFilename = "D:/Test/新建文本文档.txt";
		String newFilename = "D:/Test/newFilename.txt";
		
		File file = new File(oldFilename);
		if(file.exists()) {
			file.renameTo(new File(newFilename));
		}
	}
	
	public static void main(String[] args) {
		MyHashMap.main(null);
		MyHashtable.main(null);
	}
}
