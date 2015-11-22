
package com.prchny.base.memcached.service;

import com.prchny.base.cache.Cachable;

public interface IMemcachedService {
  
  public void initialize(String memcacheServerList) throws Exception;
  
  public <T extends Cachable> T get(String key, Class<T> cachable);
  
  public Object get(String key);
  
  public void put(String key, Cachable value, int secondToLive);
  
  public boolean keyExists(String key);
  
  public void remove(String key) throws Exception;
  
  void put(String key, Object value, int secondToLive);
  
  void increment(String key, int amount);
  
  /**
   * @param key
   * @return
   */
  Integer getInt(String key);
  
  /**
   * @param key
   * @param value
   * @param secondToLive
   */
  void putInt(String key, Integer value, int secondToLive);
  
}
