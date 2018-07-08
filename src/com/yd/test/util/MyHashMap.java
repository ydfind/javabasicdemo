package com.yd.test.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashMap;

public class MyHashMap<K, V> extends MyAbstractMap<K, V> implements MyMap<K, V> {

	private static final long serialVersionUID = 362498820763181265L;

	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16
	static final int MAXIMUM_CAPACITY = 1 << 30;
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int TREEIFY_THRESHOLD = 8;
	static final int UNTREEIFY_THRESHOLD = 6;
	static final int MIN_TREEIFY_CAPACITY = 64;

	transient MyNode<K, V>[] table;
	transient Set<MyMap.MyEntry<K, V>> entrySet;
	transient int size;
	transient int modCount;
	int threshold;
	final float loadFactor;

	public MyHashMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}
		this.loadFactor = loadFactor;
		this.threshold = tableSizeFor(initialCapacity);
	}
	
	public MyHashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	public MyHashMap() {
		this.loadFactor = DEFAULT_LOAD_FACTOR;
	}

	// DRD: 计算出最小的2的幂次方，使其刚好大于等于cap
	static final int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1: (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}

	static class MyNode<K, V> implements MyMap.MyEntry<K, V> {
		final int hash;
		final K key;
		V value;
		MyNode<K, V> next;

		MyNode(int hash, K key, V value, MyNode<K, V> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof MyMap.MyEntry) {
				MyMap.MyEntry<?, ?> e = (MyMap.MyEntry<?, ?>) o;
				if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
					return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key) ^ Objects.hashCode(value);
		}

	}

//	static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V>{
//		MyTreeNode
//	}
	
	static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		MyNode<K, V> e;
		e = getNode(hash(key), key);
		return e == null ? null : e.value;
	}

	final MyNode<K, V> getNode(int hash, Object key) {
		// TODO Auto-generated method stub
		MyNode<K, V>[] tab;
		MyNode<K, V> first, e;
		int n;
		K k;
		tab = table;
		n = tab.length;
		if(tab != null && n > 0 && (first = tab[(n - 1) & hash]) != null) {
			
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return putVal(hash(key), key, value, false, true);
	}

	private V putVal(int hash, K key, V value, boolean onlyIfAbsent, 
			boolean evict) {
		MyNode<K, V>[] tab;
		MyNode<K, V>[] p;
		int n, i;
		tab = table;
		n = tab.length;
		if(tab == null || n == 0) {
			n = (tab = resize()).length;
		}
		return null;
	}

	private MyNode<K, V>[] resize() {
//		// TODO Auto-generated method stub
//		MyNode<K, V>[] oldTab = table;
//		int oldCap = (oldTab == null) ? 0 : oldTab.length;
//		int oldThr = threshold;
//		int newCap, newThr = 0;
//		if(oldCap > 0) {
//			if(oldCap >= MAXIMUM_CAPACITY) {
//				threshold = Integer.MAX_VALUE;
//				return oldTab;
//				
//			}
//			else if((newCap = oldCap << 1) < MAXIMUM_CAPACITY && 
//					oldCap >= DEFAULT_INITIAL_CAPACITY)
//				newThr = oldThr << 1;
//		}
//		else if(oldThr > 0) {
//			newCap = oldThr;
//		}
//		else {
//			newCap = DEFAULT_INITIAL_CAPACITY;
//			newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//			
//		}
//		if(newThr == 0) {
//			float ft = (float)newCap * loadFactor;
//			newThr = newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY
//					? (int)ft: Integer.MAX_VALUE;
//		}
//		threshold = newThr;
//		@SuppressWarnings({"rawtypes","unchecked"})
//        MyNode<K,V>[] newTab = (MyNode<K,V>[])new MyNode[newCap];
//	    table = newTab;
//	    if (oldTab != null) {
//	        for (int j = 0; j < oldCap; ++j) {
//	            MyNode<K,V> e;
//	            if ((e = oldTab[j]) != null) {
//	                oldTab[j] = null;
//	                if (e.next == null)
//	                    newTab[e.hash & (newCap - 1)] = e;
//	                else if (e instanceof TreeNode)
//	                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//	                else { // preserve order
//	                    Node<K,V> loHead = null, loTail = null;
//	                    Node<K,V> hiHead = null, hiTail = null;
//	                    Node<K,V> next;
//	                    do {
//	                        next = e.next;
//	                        if ((e.hash & oldCap) == 0) {
//	                            if (loTail == null)
//	                                loHead = e;
//	                            else
//	                                loTail.next = e;
//	                            loTail = e;
//	                        }
//	                        else {
//	                            if (hiTail == null)
//	                                hiHead = e;
//	                            else
//	                                hiTail.next = e;
//	                            hiTail = e;
//	                        }
//	                    } while ((e = next) != null);
//	                    if (loTail != null) {
//	                        loTail.next = null;
//	                        newTab[j] = loHead;
//	                    }
//	                    if (hiTail != null) {
//	                        hiTail.next = null;
//	                        newTab[j + oldCap] = hiHead;
//	                    }
//	                }
//	            }
//	        }
//	    }
//	    return newTab;
		return null;
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(MyMap<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<MyEntry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
