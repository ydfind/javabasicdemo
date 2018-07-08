package com.yd.test.util;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public interface MyMap<K, V> {
	int size();
	boolean isEmpty();
	boolean containsKey(Object key);
	boolean containsValue(Object value);
	V get(Object key);
	V put(K key, V value);
	V remove(Object key);
	void putAll(MyMap<? extends K, ? extends V> m);
	void clear();
	Set<K> keySet();
	Collection<V> values();
	Set<MyMap.MyEntry<K, V>> entrySet();
	interface MyEntry<K, V>{
		K getKey();
		V getValue();
		V setValue(V value);
		boolean equals(Object o);
		int hashCode();
//		public static <K extends Comparable<? super K>, V> 
//		  Comparator<MyMap.MyEntry<K, V>> 
//		comparingByKey(){
//            return (Comparator<MyMap.MyEntry<K, V>> & Serializable)
//                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
//		}
		
	}
	
	boolean equals(Object o);
	int hashCode();
	default V getOrDefault(Object key, V defaultValue) {
		V v;
		v = get(key);
		return ((v != null) || containsKey(key))
				? v : defaultValue;
	}
	
//	default void forEach(BiConsumer<? super K, ? super V> action) {
//		Objects.requireNonNull(action);
//		for(MyMap.MyEntry<K, V> entry: entrySet()) {
//			K k;
//			V v;
//			try {
//				k = entry.getKey();
//				v = entry.getValue();
//			}catch(IllegalStateException ise) {
//				throw new ConcurrentModificationException(ise);
//			}
//			action.accept(k, v);
//		}
//	}
	
	default boolean remove(Object key, Object value) {
		Object curValue = get(key);
		if(!Objects.equals(curValue, value) ||
				(curValue == null && !containsKey(key))) {
			// DRD : !containsKey(key)成立，不是必然curValue == null？？？？
			return false;			
		}
		remove(key);
		return true;
	}

}
