/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 10-May-2012
 *  @author rahul
 */

package com.prchny.base.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class HttpClientPropertiesUtil extends PropertyPlaceholderConfigurer {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(HttpClientPropertiesUtil.class);
  
  private static Map<String, String> propertiesMap =
      new HashMap<String, String>();
  
  public static final String PROPERTY_DEFAULT_MAX_PER_ROUTE =
      "defaultMaxPerRoute";
  
  public static final String PROPERTY_PROTOCOL = "protocol";
  
  public static final String PROPERTY_MAX_TOTAL_CONNECTIONS =
      "maxTotalConnections";
  
  public static final String PROPERTY_SO_TIMEOUT = "soTimeout";
  
  public static final String PROPERTY_KEEP_ALIVE = "keepAlive";
  
  public static final String PROPERTY_IDLE_CONNECTION_MONITOR_ON =
      "idleConnectionMonitorOn";
  
  public static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";
  
  @Override
  protected void processProperties(
      final ConfigurableListableBeanFactory beanFactory, final Properties props)
      throws BeansException {
  
    super.processProperties(beanFactory, props);
    LOG.info("loading httpclient properties file");
    for (final Object key : props.keySet()) {
      final String keyStr = key.toString();
      propertiesMap.put(keyStr, props.getProperty(keyStr));
    }
  }
  
  public static String getProperty(final String name) {
  
    return propertiesMap.get(name);
  }
  
}
