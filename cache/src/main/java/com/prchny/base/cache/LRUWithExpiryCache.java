package com.prchny.base.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUWithExpiryCache<K, V> {
  
  private final Map<K, V> cacheMap;
  
  private final LRUCache<K, Long> entryTime;
  
  private long expiryThresholdInMilliseconds;
  
  public LRUWithExpiryCache(final int cacheSize,
      final int expiryThresholdInMilliseconds) {
  
    // true = use access order instead of insertion order.
    this.cacheMap = new LinkedHashMap<K, V>(cacheSize, 0.75f, true) {
      
      private static final long serialVersionUID = 5788663448493283140L;
      
      @Override
      protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
      
        // When to remove the eldest entry.
        return size() > cacheSize; // Size exceeded the max allowed.
      }
    };
    this.entryTime = new LRUCache<K, Long>(cacheSize);
    this.expiryThresholdInMilliseconds = expiryThresholdInMilliseconds;
  }
  
  public void put(final K key, final V elem) {
  
    cacheMap.put(key, elem);
    entryTime.put(key, System.currentTimeMillis());
  }
  
  public V get(final K key) {
  
    final Long entryTimeInMs = entryTime.get(key);
    if (entryTimeInMs != null) {
      final V v = cacheMap.get(key);
      if (System.currentTimeMillis() < (entryTimeInMs.longValue() + expiryThresholdInMilliseconds)) {
        return v;
      }
    }
    
    return null;
  }
  
  public boolean hasHalfTimeExpired(final K key) {
  
    final Long entryTimeInMs = entryTime.get(key);
    if (entryTimeInMs != null) {
      final V v = cacheMap.get(key);
      if (System.currentTimeMillis() > (entryTimeInMs.longValue() + (expiryThresholdInMilliseconds / 2))) {
        return true;
      }
    }
    
    return false;
  }
  
  public static void main(final String args[]) {
  
    final LRUWithExpiryCache<Integer, Integer> cache =
        new LRUWithExpiryCache<Integer, Integer>(2, 1);
    
    for (int i = 0; i < 5; i++) {
      cache.put(i, i);
      System.out.println(cache.cacheMap);
      for (int j = 0; j <= i; j++) {
        System.out.println(j + "--" + cache.get(j));
      }
    }
  }
  
}
