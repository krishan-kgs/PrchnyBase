/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 10-May-2012
 *  @author rahul
 */

package com.prchny.base.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ThreadPoolProperties extends PropertyPlaceholderConfigurer {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ThreadPoolProperties.class);
  
  private static Map<String, String> propertiesMap =
      new HashMap<String, String>();
  
  public static final String PROPERTY_ENABLED = "enabled";
  
  public static final String PROPERTY_CORE_POOL_SIZE = "corePoolSize";
  
  public static final String PROPERTY_MAX_POOL_SIZE = "maxPoolSize";
  
  public static final String PROPERTY_KEEP_ALIVE_TIME_IN_MIN =
      "keepAliveTimeInMin";
  
  public static final String PROPERTY_QUEUE_SIZE = "queueSize";
  
  @Override
  protected void processProperties(
      final ConfigurableListableBeanFactory beanFactory, final Properties props)
      throws BeansException {
  
    super.processProperties(beanFactory, props);
    LOG.info("loading threadPoolProperties properties file");
    for (final Object key : props.keySet()) {
      final String keyStr = key.toString();
      propertiesMap.put(keyStr, props.getProperty(keyStr));
    }
  }
  
  public static String getProperty(final String name) {
  
    return propertiesMap.get(name);
  }
  
}
