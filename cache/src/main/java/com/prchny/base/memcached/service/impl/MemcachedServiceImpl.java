
package com.prchny.base.memcached.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prchny.base.cache.Cachable;
import com.prchny.base.memcached.service.IMemcachedService;

@Service("memcachedService")
public class MemcachedServiceImpl implements IMemcachedService {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(MemcachedServiceImpl.class);
  
  private static final int NO_OF_INSTANCES = 30;
  
  private List<MemcachedClient> memcachedClients;
  
  private String serverList;
  
  private String makeupKey(final String key) {
  
    return StringUtils.trimAllWhitespace(key);
  }
  
  private MemcachedClient getRandomClient() {
  
    final int random = (int) (Math.random() * NO_OF_INSTANCES);
    return memcachedClients.get(random);
  }
  
  /**
   * Gets a value of an element which matches the given key.
   * 
   * @param key
   *          the key of the element to return.
   * @return The value placed into the cache with an earlier put, or null if not
   *         found or expired
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T extends Cachable> T get(final String key, final Class<T> cachable) {
  
    if (LOG.isDebugEnabled()) {
      LOG.debug("get:: key: {}", key);
    }
    if (key != null) {
      final String str = (String) getRandomClient().get(makeupKey(key));
      if (str != null) {
        try {
          return (T) cachable.newInstance().loadFromMemString(str);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }
  
  /**
   * Puts a cachable object into the cache
   */
  @Override
  public void put(final String key, final Cachable cachable,
      final int secondToLive) {
  
    // String json = new Gson().toJson(cachable);
    // LOG.debug("put:: key: {} value: {}", key, json);
    getRandomClient().set(makeupKey(key), secondToLive, cachable.toMemString());
  }
  
  /**
   * Hacky way to check whether the key exists or not
   * 
   * @param key
   * @return
   */
  @Override
  public boolean keyExists(final String key) {
  
    return (getRandomClient().get(key) != null);
  }
  
  /**
   * Removes the element which matches the key.
   * <p/>
   * If no element matches, nothing is removed and no Exception is thrown.
   * 
   * @param key
   *          the key of the element to remove
   * @throws Exception
   */
  @Override
  public void remove(final String key) {
  
    getRandomClient().delete(makeupKey(key));
  }
  
  @Override
  public void initialize(final String memcacheServerList) throws Exception {
  
    if (memcacheServerList.equals(serverList)) {
      return;
    }
    
    serverList = memcacheServerList;
    
    LOG.info("Initializing Memcached Service, servers : {} ...",
        memcacheServerList);
    if (memcachedClients != null) {
      for (int i = 0; i < memcachedClients.size(); i++) {
        memcachedClients.get(i).shutdown();
      }
      memcachedClients.clear();
    } else {
      memcachedClients = new ArrayList<MemcachedClient>(NO_OF_INSTANCES);
    }
    for (int i = 0; i < NO_OF_INSTANCES; i++) {
      final MemcachedClient mc =
          new MemcachedClient(AddrUtil.getAddresses(memcacheServerList));
      memcachedClients.add(mc);
    }
    LOG.info("Memcached Service Initialized... SUCCESSFULLY...");
  }
  
  @Override
  protected void finalize() throws Throwable {
  
    for (int i = 0; i < memcachedClients.size(); i++) {
      memcachedClients.get(i).shutdown();
    }
    memcachedClients.clear();
  }
  
  @Override
  public Object get(final String key) {
  
    if (LOG.isDebugEnabled()) {
      LOG.debug("get:: key: {}", key);
    }
    if (key != null) {
      try {
        return getRandomClient().get(makeupKey(key));
      } catch (final Throwable t) {
        LOG.error("Error while retrieving the key from memcache", t);
        return null;
      }
    }
    return null;
  }
  
  @Override
  public Integer getInt(final String key) {
  
    final Object value = get(key);
    if (value != null) {
      try {
        return Integer.parseInt(String.valueOf(value));
      } catch (final NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }
  
  @Override
  public void increment(final String key, final int amount) {
  
    if (amount > 0) {
      getRandomClient().asyncIncr(makeupKey(key), amount);
    } else {
      getRandomClient().asyncDecr(makeupKey(key), -(amount));
    }
    
  }
  
  @Override
  public void put(final String key, final Object value, final int secondToLive) {
  
    getRandomClient().set(makeupKey(key), secondToLive, value);
  }
  
  @Override
  public void putInt(final String key, final Integer value,
      final int secondToLive) {
  
    put(key, String.valueOf(value), secondToLive);
  }
  
  public static void main(final String[] args) throws IOException,
      InterruptedException {
  
    final MemcachedClient mc =
        new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true)
            .build(), AddrUtil.getAddresses("192.168.2.251:11211"));
    final String key = "mobile/9868589788";
    System.out.println(mc.get(key));
    mc.set(key, 30 * 24 * 60 * 60, "2");
    System.out.println(mc.get(key));
    mc.asyncIncr(key, 1);
    System.out.println("incr: " + mc.get(key));
    mc.asyncDecr(key, 1);
    System.out.println(mc.get(key));
    mc.asyncIncr(key, 5);
    mc.delete(key);
  }
  
}
