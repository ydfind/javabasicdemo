package com.yd.test.util;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public abstract class MyAbstractMap<K, V> implements MyMap<K, V> {
	
	protected MyAbstractMap() {
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return entrySet().size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
		if(key == null) {
			while(i.hasNext()) {
				MyEntry<K, V> e = i.next();
				if(e.getKey() == null)
					return true;
			}
		}else {
			while(i.hasNext()) {
				MyEntry<K, V> e = i.next();
				if(key.equals(e.getKey()))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		Iterator<MyEntry<K,V>> i = entrySet().iterator();
        if (value==null) {
            while (i.hasNext()) {
            	MyEntry<K,V> e = i.next();
                if (e.getValue()==null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
            	MyEntry<K,V> e = i.next();
                if (value.equals(e.getValue()))
                    return true;
            }
        }
        return false;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
		if(key == null) {
			while(i.hasNext()) {
				MyEntry<K, V> e = i.next();
				if(e.getKey() == null)
					return e.getValue();
			}
		}else {
			while(i.hasNext()) {
				MyEntry<K, V> e = i.next();
				if(key.equals(e.getKey()))
					return e.getValue();
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		Iterator<MyEntry<K,V>> i = entrySet().iterator();
		MyEntry<K,V> correctEntry = null;
        if (key==null) {
            while (correctEntry==null && i.hasNext()) {
            	MyEntry<K,V> e = i.next();
                if (e.getKey()==null)
                    correctEntry = e;
            }
        } else {
            while (correctEntry==null && i.hasNext()) {
            	MyEntry<K,V> e = i.next();
                if (key.equals(e.getKey()))
                    correctEntry = e;
            }
        }

        V oldValue = null;
        if (correctEntry !=null) {
            oldValue = correctEntry.getValue();
            i.remove();
        }
        return oldValue;
	}

	@Override
	public void putAll(MyMap<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		for(MyMap.MyEntry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		entrySet().clear();
	}
	// DRD: 将不会被系列化
	transient Set<K> keySet;
	transient Collection<V> values;

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		Set<K> ks = keySet;
		if(ks == null) {
			ks = new AbstractSet<K>() {
				public Iterator<K> iterator(){
					return new Iterator<K>(){
						private Iterator<MyEntry<K, V>> i = entrySet().iterator();
						public boolean hasNext() {
							return i.hasNext();
						}
						public K next() {
							return i.next().getKey();
						}
						public void remove() {
							i.remove();
						}
					};
				}
				public int size() {
					return MyAbstractMap.this.size();
				}
				public boolean isEmpty() {
                    return MyAbstractMap.this.isEmpty();
                }

                public void clear() {
                	MyAbstractMap.this.clear();
                }

                public boolean contains(Object k) {
                    return MyAbstractMap.this.containsKey(k);
                }
			};
			keySet = ks;
		}
		return ks;
	}

	@Override
	public Collection<V> values() {
        Collection<V> vals = values;
        if (vals == null) {
            vals = new AbstractCollection<V>() {
                public Iterator<V> iterator() {
                    return new Iterator<V>() {
                        private Iterator<MyEntry<K,V>> i = entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public V next() {
                            return i.next().getValue();
                        }

                        public void remove() {
                            i.remove();
                        }
                    };
                }

                public int size() {
                    return MyAbstractMap.this.size();
                }

                public boolean isEmpty() {
                    return MyAbstractMap.this.isEmpty();
                }

                public void clear() {
                	MyAbstractMap.this.clear();
                }

                public boolean contains(Object v) {
                    return MyAbstractMap.this.containsValue(v);
                }
            };
            values = vals;
        }
        return vals;
	}

	public abstract Set<MyEntry<K,V>> entrySet();

}
