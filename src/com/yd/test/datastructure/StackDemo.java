package com.yd.test.datastructure;

import java.util.EmptyStackException;
import java.util.Stack;

public class StackDemo {

	static void showpush(Stack st, int a) {
		st.push(new Integer(a));
		System.out.println("push(" + a + ")");
		System.out.println("Stack after push: " + st);
	}

	static void showpop(Stack st) {
		System.out.print("pop -> ");
		Integer a = (Integer) st.pop();
		System.out.println(a);
		System.out.println("Stack after pop: " + st);
	}

	public static void main(String[] args) {
		Stack st = new Stack();
		System.out.println("Stack start: " + st);
		showpush(st, 42);
		showpush(st, 66);
		showpush(st, 99);
		showpop(st);
		showpop(st);
		showpop(st);
		try {
			showpop(st);
		} catch (EmptyStackException e) {
			System.out.println("empty stack = " + e.getMessage());
		}

	}

}
