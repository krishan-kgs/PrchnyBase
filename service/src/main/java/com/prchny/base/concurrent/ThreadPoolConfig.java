/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Nov-2012
 *  @author sunny
 */

package com.prchny.base.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPoolConfig {
  
  private static final int DEFAULT_CORE_POOL_SIZE = 5;
  
  private static final int DEFAULT_MAX_POOL_SIZE = 10;
  
  private static final long DEFAULT_KEEP_ALIVE_TIME = 10;
  
  private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
  
  private static final int DEFAULT_QUEUE_SIZE = 50;
  
  private int corePoolSize;
  
  private int maxPoolSize;
  
  private long keepAliveTimeInMin;
  
  private TimeUnit timeUnit;
  
  private BlockingQueue<Runnable> workQueue;
  
  public ThreadPoolConfig() {
  
    corePoolSize = DEFAULT_CORE_POOL_SIZE;
    maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    keepAliveTimeInMin = DEFAULT_KEEP_ALIVE_TIME;
    timeUnit = DEFAULT_TIME_UNIT;
    workQueue = new LinkedBlockingQueue<Runnable>(DEFAULT_QUEUE_SIZE);
  }
  
  public ThreadPoolConfig(final int corePoolSize, final int maxPoolSize,
      final long keepAliveTimeInMin, final int queueSize) {
  
    this.corePoolSize = corePoolSize;
    this.maxPoolSize = maxPoolSize;
    this.keepAliveTimeInMin = keepAliveTimeInMin;
    timeUnit = DEFAULT_TIME_UNIT;
    workQueue = new LinkedBlockingQueue<Runnable>(queueSize);
  }
  
  public int getCorePoolSize() {
  
    return corePoolSize;
  }
  
  public void setCorePoolSize(final int corePoolSize) {
  
    this.corePoolSize = corePoolSize;
  }
  
  public int getMaxPoolSize() {
  
    return maxPoolSize;
  }
  
  public void setMaxPoolSize(final int maxPoolSize) {
  
    this.maxPoolSize = maxPoolSize;
  }
  
  public long getKeepAliveTimeInMin() {
  
    return keepAliveTimeInMin;
  }
  
  public void setKeepAliveTimeInMin(final long keepAliveTimeInMin) {
  
    this.keepAliveTimeInMin = keepAliveTimeInMin;
  }
  
  public TimeUnit getTimeUnit() {
  
    return timeUnit;
  }
  
  public void setTimeUnit(final TimeUnit timeUnit) {
  
    this.timeUnit = timeUnit;
  }
  
  public BlockingQueue<Runnable> getWorkQueue() {
  
    return workQueue;
  }
  
  public void setWorkQueue(final BlockingQueue<Runnable> workQueue) {
  
    this.workQueue = workQueue;
  }
  
}
