package com.yd.test.datastructure;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapDemo {
	public static void main(String[] args) {
		Map m1 = new LinkedHashMap();
		m1.put("Zara", "8");
		m1.put("Mahnaz", "31");
		m1.put("Ayan", "12");
		m1.put("Daisy", "14");
		System.out.println();
		System.out.println("Map Elements");
		System.out.println("\t" + m1);
	}

}
