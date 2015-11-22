package com.prchny.base.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUCache<K, V> {
  
  private final Map<K, V> cacheMap;
  
  public LRUCache(final int cacheSize) {
  
    // true = use access order instead of insertion order.
    this.cacheMap = new LinkedHashMap<K, V>(cacheSize, 0.75f, true) {
      
      private static final long serialVersionUID = 123234454352323L;
      
      @Override
      protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
      
        // When to remove the eldest entry.
        return size() > cacheSize; // Size exceeded the max allowed.
      }
    };
  }
  
  public void put(final K key, final V elem) {
  
    cacheMap.put(key, elem);
  }
  
  public V get(final K key) {
  
    return cacheMap.get(key);
  }
  
  public void remove(final K key) {
  
    cacheMap.remove(key);
  }
  
  public int size() {
  
    return cacheMap.size();
  }
  
  public Set<K> keySet() {
  
    return cacheMap.keySet();
  }
  
}
