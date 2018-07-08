package com.yd.test.util.Map;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class MyAbstractMap<K,V> implements MyMap<K,V> {
    
    protected MyAbstractMap() {
    }

    // Query Operations

    
    public int size() {
        return entrySet().size();
    }

    
    public boolean isEmpty() {
        return size() == 0;
    }

    
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

    
    public boolean containsKey(Object key) {
        Iterator<MyMap.MyEntry<K,V>> i = entrySet().iterator();
        if (key==null) {
            while (i.hasNext()) {
                MyEntry<K,V> e = i.next();
                if (e.getKey()==null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                MyEntry<K,V> e = i.next();
                if (key.equals(e.getKey()))
                    return true;
            }
        }
        return false;
    }

    
    public V get(Object key) {
        Iterator<MyEntry<K,V>> i = entrySet().iterator();
        if (key==null) {
            while (i.hasNext()) {
                MyEntry<K,V> e = i.next();
                if (e.getKey()==null)
                    return e.getValue();
            }
        } else {
            while (i.hasNext()) {
                MyEntry<K,V> e = i.next();
                if (key.equals(e.getKey()))
                    return e.getValue();
            }
        }
        return null;
    }


    // Modification Operations

    
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    
    public V remove(Object key) {
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


    // Bulk Operations

    
    public void putAll(MyMap<? extends K, ? extends V> m) {
        for (MyMap.MyEntry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    
    public void clear() {
        entrySet().clear();
    }


    // Views

    
    transient Set<K>        keySet;
    transient Collection<V> values;

    
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new AbstractSet<K>() {
                public Iterator<K> iterator() {
                    return new Iterator<K>() {
                        private Iterator<MyEntry<K,V>> i = entrySet().iterator();

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


    // Comparison and hashing

    
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof MyMap))
            return false;
        MyMap<?,?> m = (MyMap<?,?>) o;
        if (m.size() != size())
            return false;

        try {
            Iterator<MyEntry<K,V>> i = entrySet().iterator();
            while (i.hasNext()) {
                MyEntry<K,V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key)==null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    
    public int hashCode() {
        int h = 0;
        Iterator<MyEntry<K,V>> i = entrySet().iterator();
        while (i.hasNext())
            h += i.next().hashCode();
        return h;
    }

    
    public String toString() {
        Iterator<MyEntry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            MyEntry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key   == this ? "(this MyMap)" : key);
            sb.append('=');
            sb.append(value == this ? "(this MyMap)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    
    protected Object clone() throws CloneNotSupportedException {
        MyAbstractMap<?,?> result = (MyAbstractMap<?,?>)super.clone();
        result.keySet = null;
        result.values = null;
        return result;
    }

    
    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    // Implementation Note: SimpleEntry and SimpleImmutableEntry
    // are distinct unrelated classes, even though they share
    // some code. Since you can't add or subtract final-ness
    // of a field in a subclass, they can't share representations,
    // and the amount of duplicated code is too small to warrant
    // exposing a common abstract class.


    
    public static class SimpleEntry<K,V>
        implements MyEntry<K,V>, java.io.Serializable
    {
        private static final long serialVersionUID = -8499721149061103585L;

        private final K key;
        private V value;

        
        public SimpleEntry(K key, V value) {
            this.key   = key;
            this.value = value;
        }

        
        public SimpleEntry(MyEntry<? extends K, ? extends V> entry) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        
        public K getKey() {
            return key;
        }

        
        public V getValue() {
            return value;
        }

        
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        
        public boolean equals(Object o) {
            if (!(o instanceof MyMap.MyEntry))
                return false;
            MyMap.MyEntry<?,?> e = (MyMap.MyEntry<?,?>)o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }

        
        public int hashCode() {
            return (key   == null ? 0 :   key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }

        
        public String toString() {
            return key + "=" + value;
        }

    }

    
    public static class SimpleImmutableEntry<K,V>
        implements MyEntry<K,V>, java.io.Serializable
    {
        private static final long serialVersionUID = 7138329143949025153L;

        private final K key;
        private final V value;

        
        public SimpleImmutableEntry(K key, V value) {
            this.key   = key;
            this.value = value;
        }

        
        public SimpleImmutableEntry(MyEntry<? extends K, ? extends V> entry) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        
        public K getKey() {
            return key;
        }

        
        public V getValue() {
            return value;
        }

        
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        
        public boolean equals(Object o) {
            if (!(o instanceof MyMap.MyEntry))
                return false;
            MyMap.MyEntry<?,?> e = (MyMap.MyEntry<?,?>)o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }

        
        public int hashCode() {
            return (key   == null ? 0 :   key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }

        
        public String toString() {
            return key + "=" + value;
        }

    }

}
