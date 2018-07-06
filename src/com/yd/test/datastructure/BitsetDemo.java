package com.yd.test.datastructure;

import java.util.BitSet;

public class BitsetDemo {
	public static void main(String[] args) {
		BitSet bits1 = new BitSet(16);
		BitSet bits2 = new BitSet(16);
		for(int i = 0; i < 16; i++) {
			if(i % 2 == 0)
				bits1.set(i);
			if(i % 5 == 0)
				bits2.set(i);
		}
		System.out.println("Initial pattern in bits1: " + bits1);
		System.out.println("Initial pattern in bits2: " + bits2);
		bits2.and(bits1);
		System.out.println("\nInitial pattern in bits2.and(bits1): " + bits2);
		bits2.or(bits1);
		System.out.println("\nInitial pattern in bits2.or(bits1): " + bits2);
		bits2.xor(bits1);
		System.out.println("\nInitial pattern in bits2.xor(bits1): " + bits2);	
	}

}
