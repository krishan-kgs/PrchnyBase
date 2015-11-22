/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 10-May-2012
 *  @author rahul
 */

package com.prchny.base.model.common;

import java.net.InetAddress;

public class HttpClientConfig {
  
  private int defaultMaxPerRoute = 50;
  
  private int maxTotalConnections = 100;
  
  private InetAddress localAddress;
  
  private int soTimeout = 30 * 1000;
  
  private long keepAlive = -1;
  
  private boolean idleConnectionMonitorOn = true;
  
  public int getDefaultMaxPerRoute() {
  
    return defaultMaxPerRoute;
  }
  
  public void setDefaultMaxPerRoute(final int defaultMaxPerRoute) {
  
    this.defaultMaxPerRoute = defaultMaxPerRoute;
  }
  
  public int getMaxTotalConnections() {
  
    return maxTotalConnections;
  }
  
  public void setMaxTotalConnections(final int maxTotalConnections) {
  
    this.maxTotalConnections = maxTotalConnections;
  }
  
  public InetAddress getLocalAddress() {
  
    return localAddress;
  }
  
  public void setLocalAddress(final InetAddress localAddress) {
  
    this.localAddress = localAddress;
  }
  
  public int getSoTimeout() {
  
    return soTimeout;
  }
  
  public void setSoTimeout(final int soTimeout) {
  
    this.soTimeout = soTimeout;
  }
  
  public long getKeepAlive() {
  
    return keepAlive;
  }
  
  public void setKeepAlive(final long keepAlive) {
  
    this.keepAlive = keepAlive;
  }
  
  public boolean isIdleConnectionMonitorOn() {
  
    return idleConnectionMonitorOn;
  }
  
  public void setIdleConnectionMonitorOn(final boolean idleConnectionMonitorOn) {
  
    this.idleConnectionMonitorOn = idleConnectionMonitorOn;
  }
  
  @Override
  public String toString() {
  
    return "HttpClientConfig [defaultMaxPerRoute=" + defaultMaxPerRoute
        + ", maxTotalConnections=" + maxTotalConnections + ", localAddress="
        + localAddress + ", soTimeout=" + soTimeout + ", keepAlive="
        + keepAlive + ", idleConnectionMonitorOn=" + idleConnectionMonitorOn
        + "]";
  }
}
