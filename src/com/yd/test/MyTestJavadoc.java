package com.yd.test;
/**
 * 第一个程序
 * @author YDIAN
 *
 */
public class MyTestJavadoc {
	/**
	 * 计算两个值的和
	 * @param a 参数1
	 * @param b 参数2
	 * @return 返回两个参数的和
	 */
	private static int add(int a, int b) {
		return a + b;
	}
	
	/**
	 * 测试两个值的和
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("5 + 3 = " + add(5, 3));
	}

}
