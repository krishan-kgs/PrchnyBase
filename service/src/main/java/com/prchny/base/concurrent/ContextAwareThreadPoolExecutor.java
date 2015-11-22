/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 28-Nov-2012
 *  @author sunny
 */

package com.prchny.base.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.MDC;

public class ContextAwareThreadPoolExecutor extends ThreadPoolExecutor {
  
  public ContextAwareThreadPoolExecutor(final ThreadPoolConfig config) {
  
    super(config.getCorePoolSize(), config.getMaxPoolSize(), config
        .getKeepAliveTimeInMin(), config.getTimeUnit(), config.getWorkQueue());
  }
  
  @Override
  public void execute(final Runnable command) {
  
    final ContextAwareRunnable r = new ContextAwareRunnable();
    r.setRequestId(MDC.get("requestId"));
    r.setRunnable(command);
    super.execute(r);
  }
}
