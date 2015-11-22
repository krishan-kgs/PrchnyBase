/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 28-Nov-2012
 *  @author sunny
 */

package com.prchny.base.concurrent;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextAwareThreadPoolExecutorFactory {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ContextAwareThreadPoolExecutorFactory.class);
  
  Map<String, ContextAwareThreadPoolExecutor> executors =
      new HashMap<String, ContextAwareThreadPoolExecutor>();
  
  private static ContextAwareThreadPoolExecutorFactory factory =
      new ContextAwareThreadPoolExecutorFactory();
  
  private ContextAwareThreadPoolExecutorFactory() {
  
  }
  
  public static ContextAwareThreadPoolExecutorFactory getInstance() {
  
    return factory;
  }
  
  public ContextAwareThreadPoolExecutor getExecutor(final String name) {
  
    ContextAwareThreadPoolExecutor executor = executors.get(name);
    final String propertyServerEnabled =
        name + "." + ThreadPoolProperties.PROPERTY_ENABLED;
    if (executor != null) {
      return executor;
    } else if (ThreadPoolProperties.getProperty(propertyServerEnabled) != null) {
      try {
        final int corePoolSize =
            Integer.parseInt(ThreadPoolProperties.getProperty(name + "."
                + ThreadPoolProperties.PROPERTY_CORE_POOL_SIZE));
        final int maxPoolSize =
            Integer.parseInt(ThreadPoolProperties.getProperty(name + "."
                + ThreadPoolProperties.PROPERTY_MAX_POOL_SIZE));
        final long keepAliveTime =
            Long.parseLong(ThreadPoolProperties.getProperty(name + "."
                + ThreadPoolProperties.PROPERTY_KEEP_ALIVE_TIME_IN_MIN));
        final int queueSize =
            Integer.parseInt(ThreadPoolProperties.getProperty(name + "."
                + ThreadPoolProperties.PROPERTY_QUEUE_SIZE));
        final ThreadPoolConfig config =
            new ThreadPoolConfig(corePoolSize, maxPoolSize, keepAliveTime,
                queueSize);
        executor = new ContextAwareThreadPoolExecutor(config);
      } catch (final Exception e) {
        LOG.error("Error reading thread pool properties from property file");
        executor = new ContextAwareThreadPoolExecutor(new ThreadPoolConfig());
      }
    } else {
      executor = new ContextAwareThreadPoolExecutor(new ThreadPoolConfig());
    }
    executors.put(name, executor);
    return executor;
  }
}
