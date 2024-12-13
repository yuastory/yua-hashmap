package hashmap;

import java.util.*;

public class YuaHashMap<K, V>
{
    private Set<Entry<K, V>> entrySet;
    Set<K> keySet;
    Collection<V> values;
    final int capacity;
    Entry[] table;
    int size;
    YuaHashMap(int c)
    {
        capacity = c;
        table = new Entry[c];
    }
    
    public V put(K key, V value)
    {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K, V> e = table[i]; e != null; e = e.next)
        {
            Object k;
            if(hash == e.hash && ((k = e.key) == key || k.equals(key)))
            {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        addEntry(hash, key, value, i);
        return null;
    }
    
    void addEntry(int hash, K key, V value, int bucketIndex)
    {
        Entry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<K, V>(hash, key, value, e);
        size++;
    }
    
    V get(Object key)
    {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K, V> e = table[i]; e != null; e = e.next)
        {
            Object k;
            if (hash == e.hash && ((k = e.key) == key || k.equals(key)))
            {
                return e.value;
            }
        }
        
        return null;
    }
    
    public V remove(Object key)
    {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        Entry<K, V> prev = table[i];
        Entry<K, V> e = prev;
        while (e != null)
        {
            Entry<K, V> next = e.next;
            Object k;
            if (hash == e.hash && ((k = e.key) == key || k.equals(key)))
            {
                if (prev == e) table[i] = next;
                else prev.next = next;
                size--;
                return e.value;
            }
        }
        return null;
    }
    
    public boolean containsKey(Object key)
    {
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K, V> e = table[i]; e != null; e = e.next)
        {
            Object k;
            if (hash == e.hash && ((k = e.key) == key || k.equals(key)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean containsValue(Object value)
    {
        Entry[] t = table;
        for(int i = 0; i < t.length; i++)
        {
            for(Entry<K, V> e = t[i]; e != null; e = e.next)
            {
                if (value.equals(e.value)) return true;
            }
        }
        
        return false;
    }
    
    static public int indexFor(int hash, int index) { return hash % index; }
    private class Entry<K, V> implements Map.Entry<K, V>
    {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;
        Entry(int h, K k, V v, Entry<K, V> n)
        {
            hash = h;
            key = k;
            value = v;
            next = n;
        }
        public final K getKey() { return key; }
        public final V getValue() { return value; }
        public final V setValue(V newValue)
        {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
        
    private abstract class HashIterator<E> implements Iterator<E>
    {
        Entry<K, V> next;
        int index;
        HashIterator()
        {
            if (size > 0)
            {
                Entry[] t = table;
                while(index<t.length && (next = t[index++]) == null);
            }
        }
        
        final public boolean hasNext() { return next != null; }
        final Entry<K, V> nextEntry()
        {
            Entry<K, V> e = next;
            if((next = e.next) == null)
            {
                Entry[] t = table;
                while(index<t.length && (next = t[index++]) == null);
            }
            
            return e;
        }
    }
    
    public Set<K> keySet()
    {
        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }
    public Set<Entry<K, V>> entrySet()
    {
        Set<Entry<K, V>> ks = entrySet;
        return (ks != null ? ks : (entrySet = new EntrySet()));
    }
    public Collection<V> values()
    {
        Collection<V> ks = values;
        return (ks != null ? ks : (values = new Values()));
    }
    
    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }
    Iterator<Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }
    Iterator<V> newValuesIterator() {
        return new ValuesIterator();
    }
    
    private final class KeyIterator extends HashIterator<K>
    {
        public K next() { return nextEntry().getKey(); }
    }
    
    private final class KeySet extends AbstractSet<K>
    {
        public Iterator<K> iterator() { return newKeyIterator(); }
        
        public int size() { return size; }
    }
    private final class EntryIterator extends HashIterator<Entry<K, V>>
    {
        public Entry<K, V> next() { return nextEntry(); }
    }
    
    private final class EntrySet extends AbstractSet<Entry<K, V>>
    {
        public Iterator<Entry<K, V>> iterator() { return newEntryIterator(); }
        
        public int size() { return size; }
    }
    private final class ValuesIterator extends HashIterator<V>
    {
        public V next() { return nextEntry().value; }
    }
    
    private final class Values extends AbstractCollection<V>
    {
        public Iterator<V> iterator() { return newValuesIterator(); }
        
        public int size() { return size; }
    }
    
}