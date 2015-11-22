package com.prchny.base.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.annotations.Cache;

public class CacheManager {
  
  public static interface CacheChangeListner {
    
    public void reloadCache();
  }
  
  private CacheManager() {
  
  }
  
  private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);
  
  private static CacheManager _instance = new CacheManager();
  
  private final Map<String, Object> _caches =
      new ConcurrentHashMap<String, Object>();
  
  private static final List<CacheChangeListner> _listners =
      new ArrayList<CacheChangeListner>();
  
  public static CacheManager getInstance() {
  
    return _instance;
  }
  
  public Object getCache(final String cacheName) {
  
    final Object cache = _caches.get(cacheName);
    /*
     * if (cache == null) { throw new
     * IllegalArgumentException("cache not loaded. cache:" + cacheName); }
     */
    return cache;
  }
  
  /**
   * The same method is added since getCache() is overloaded and can create
   * problems if called from JSP. The getCacheByName() should be used in jsp
   * instead of getCache() to avoid such problems.
   * 
   * @param cacheName
   * @return
   */
  
  public Object getCacheByName(final String cacheName) {
  
    return getCache(cacheName);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getCache(final Class<T> cacheClass) {
  
    if (cacheClass.isAnnotationPresent(Cache.class)) {
      return (T) getCache(cacheClass.getAnnotation(Cache.class).name());
    } else {
      throw new IllegalArgumentException(
          "@Cache annotation should be present for cache class:"
              + cacheClass.getName());
    }
  }
  
  public synchronized void setCache(final Object cache) {
  
    final Class<? extends Object> cacheClass = cache.getClass();
    if (cacheClass.isAnnotationPresent(Cache.class)) {
      for (final Method m : cacheClass.getDeclaredMethods()) {
        if ("freeze".equals(m.getName())) {
          try {
            m.invoke(cache);
          } catch (final Exception e) {
            LOG.error("unable to freeze cache:" + cacheClass.getName(), e);
          }
        }
      }
      final Cache annotation = cacheClass.getAnnotation(Cache.class);
      _caches.put(annotation.name(), cache);
      signalListners();
    } else {
      throw new IllegalArgumentException(
          "@Cache annotation should be present for cache class:"
              + cache.getClass().getName());
    }
    
  }
  
  private void signalListners() {
  
    for (final CacheChangeListner listner : _listners) {
      try {
        listner.reloadCache();
      } catch (final Exception e) {
        LOG.error(
            "Error while signalling {} listner for cache change. Error: {}",
            listner.getClass().getName(), ExceptionUtils.getStackTrace(e));
      }
    }
  }
  
  public static void registerListner(final CacheChangeListner listner) {
  
    _listners.add(listner);
  }
  
}
