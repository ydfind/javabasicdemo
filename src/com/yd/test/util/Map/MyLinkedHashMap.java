package com.yd.test.util.Map;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MyLinkedHashMap<K,V>
    extends MyHashMap<K,V>
    implements MyMap<K,V>
{ 
    static class MyEntry<K,V> extends MyHashMap.MyNode<K,V> {
        MyEntry<K,V> before, after;
        MyEntry(int hash, K key, V value, MyNode<K,V> next) {
            super(hash, key, value, next);
        }
    }

    private static final long serialVersionUID = 3801124242820219131L;

    
    transient MyLinkedHashMap.MyEntry<K,V> head;

    
    transient MyLinkedHashMap.MyEntry<K,V> tail;

    
    final boolean accessOrder;

    // internal utilities

    // link at the end of list
    private void linkNodeLast(MyLinkedHashMap.MyEntry<K,V> p) {
        MyLinkedHashMap.MyEntry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }

    // apply src's links to dst
    private void transferLinks(MyLinkedHashMap.MyEntry<K,V> src,
                               MyLinkedHashMap.MyEntry<K,V> dst) {
        MyLinkedHashMap.MyEntry<K,V> b = dst.before = src.before;
        MyLinkedHashMap.MyEntry<K,V> a = dst.after = src.after;
        if (b == null)
            head = dst;
        else
            b.after = dst;
        if (a == null)
            tail = dst;
        else
            a.before = dst;
    }

    // overrides of MyHashMap hook methods

    void reinitialize() {
        super.reinitialize();
        head = tail = null;
    }

    MyNode<K,V> newNode(int hash, K key, V value, MyNode<K,V> e) {
        MyLinkedHashMap.MyEntry<K,V> p =
            new MyLinkedHashMap.MyEntry<K,V>(hash, key, value, e);
        linkNodeLast(p);
        return p;
    }

    MyNode<K,V> replacementNode(MyNode<K,V> p, MyNode<K,V> next) {
        MyLinkedHashMap.MyEntry<K,V> q = (MyLinkedHashMap.MyEntry<K,V>)p;
        MyLinkedHashMap.MyEntry<K,V> t =
            new MyLinkedHashMap.MyEntry<K,V>(q.hash, q.key, q.value, next);
        transferLinks(q, t);
        return t;
    }

    TreeNode<K,V> newTreeNode(int hash, K key, V value, MyNode<K,V> next) {
        TreeNode<K,V> p = new TreeNode<K,V>(hash, key, value, next);
        linkNodeLast(p);
        return p;
    }

    TreeNode<K,V> replacementTreeNode(MyNode<K,V> p, MyNode<K,V> next) {
        MyLinkedHashMap.MyEntry<K,V> q = (MyLinkedHashMap.MyEntry<K,V>)p;
        TreeNode<K,V> t = new TreeNode<K,V>(q.hash, q.key, q.value, next);
        transferLinks(q, t);
        return t;
    }

    void afterNodeRemoval(MyNode<K,V> e) { // unlink
        MyLinkedHashMap.MyEntry<K,V> p =
            (MyLinkedHashMap.MyEntry<K,V>)e, b = p.before, a = p.after;
        p.before = p.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a == null)
            tail = b;
        else
            a.before = b;
    }

    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        MyLinkedHashMap.MyEntry<K,V> first;
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }

    void afterNodeAccess(MyNode<K,V> e) { // move node to last
        MyLinkedHashMap.MyEntry<K,V> last;
        if (accessOrder && (last = tail) != e) {
            MyLinkedHashMap.MyEntry<K,V> p =
                (MyLinkedHashMap.MyEntry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }

    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after) {
            s.writeObject(e.key);
            s.writeObject(e.value);
        }
    }

    
    public MyLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }

    
    public MyLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
        accessOrder = false;
    }

    
    public MyLinkedHashMap() {
        super();
        accessOrder = false;
    }

    
    public MyLinkedHashMap(MyMap<? extends K, ? extends V> m) {
        super();
        accessOrder = false;
        putMapEntries(m, false);
    }

    
    public MyLinkedHashMap(int initialCapacity,
                         float loadFactor,
                         boolean accessOrder) {
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;
    }


    
    public boolean containsValue(Object value) {
        for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after) {
            V v = e.value;
            if (v == value || (value != null && value.equals(v)))
                return true;
        }
        return false;
    }

    
    public V get(Object key) {
        MyNode<K,V> e;
        if ((e = getNode(hash(key), key)) == null)
            return null;
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }

    
    public V getOrDefault(Object key, V defaultValue) {
       MyNode<K,V> e;
       if ((e = getNode(hash(key), key)) == null)
           return defaultValue;
       if (accessOrder)
           afterNodeAccess(e);
       return e.value;
   }

    
    public void clear() {
        super.clear();
        head = tail = null;
    }

    
    protected boolean removeEldestEntry(MyMap.MyEntry<K,V> eldest) {
        return false;
    }

    
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new LinkedKeySet();
            keySet = ks;
        }
        return ks;
    }

    final class LinkedKeySet extends AbstractSet<K> {
        public final int size()                 { return size; }
        public final void clear()               { MyLinkedHashMap.this.clear(); }
        public final Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }
        public final boolean contains(Object o) { return containsKey(o); }
        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }
        public final Spliterator<K> spliterator()  {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                                            Spliterator.ORDERED |
                                            Spliterator.DISTINCT);
        }
        public final void forEach(Consumer<? super K> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after)
                action.accept(e.key);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new LinkedValues();
            values = vs;
        }
        return vs;
    }

    final class LinkedValues extends AbstractCollection<V> {
        public final int size()                 { return size; }
        public final void clear()               { MyLinkedHashMap.this.clear(); }
        public final Iterator<V> iterator() {
            return new LinkedValueIterator();
        }
        public final boolean contains(Object o) { return containsValue(o); }
        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                                            Spliterator.ORDERED);
        }
        public final void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after)
                action.accept(e.value);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    
    public Set<MyMap.MyEntry<K,V>> entrySet() {
        Set<MyMap.MyEntry<K,V>> es;
        return (es = entrySet) == null ? (entrySet = new LinkedEntrySet()) : es;
    }

    final class LinkedEntrySet extends AbstractSet<MyMap.MyEntry<K,V>> {
        public final int size()                 { return size; }
        public final void clear()               { MyLinkedHashMap.this.clear(); }
        public final Iterator<MyMap.MyEntry<K,V>> iterator() {
            return new LinkedEntryIterator();
        }
        public final boolean contains(Object o) {
            if (!(o instanceof MyMap.MyEntry))
                return false;
            MyMap.MyEntry<?,?> e = (MyMap.MyEntry<?,?>) o;
            Object key = e.getKey();
            MyNode<K,V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(e);
        }
        public final boolean remove(Object o) {
            if (o instanceof MyMap.MyEntry) {
                MyMap.MyEntry<?,?> e = (MyMap.MyEntry<?,?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }
        public final Spliterator<MyMap.MyEntry<K,V>> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED |
                                            Spliterator.ORDERED |
                                            Spliterator.DISTINCT);
        }
        public final void forEach(Consumer<? super MyMap.MyEntry<K,V>> action) {
            if (action == null)
                throw new NullPointerException();
            int mc = modCount;
            for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after)
                action.accept(e);
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    // MyMap overrides

    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (action == null)
            throw new NullPointerException();
        int mc = modCount;
        for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after)
            action.accept(e.key, e.value);
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (function == null)
            throw new NullPointerException();
        int mc = modCount;
        for (MyLinkedHashMap.MyEntry<K,V> e = head; e != null; e = e.after)
            e.value = function.apply(e.key, e.value);
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }

    // Iterators

    abstract class LinkedHashIterator {
        MyLinkedHashMap.MyEntry<K,V> next;
        MyLinkedHashMap.MyEntry<K,V> current;
        int expectedModCount;

        LinkedHashIterator() {
            next = head;
            expectedModCount = modCount;
            current = null;
        }

        public final boolean hasNext() {
            return next != null;
        }

        final MyLinkedHashMap.MyEntry<K,V> nextNode() {
            MyLinkedHashMap.MyEntry<K,V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            current = e;
            next = e.after;
            return e;
        }

        public final void remove() {
            MyNode<K,V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }

    final class LinkedKeyIterator extends LinkedHashIterator
        implements Iterator<K> {
        public final K next() { return nextNode().getKey(); }
    }

    final class LinkedValueIterator extends LinkedHashIterator
        implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

    final class LinkedEntryIterator extends LinkedHashIterator
        implements Iterator<MyMap.MyEntry<K,V>> {
        public final MyMap.MyEntry<K,V> next() { return nextNode(); }
    }


}
