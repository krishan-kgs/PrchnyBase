/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 28-Nov-2012
 *  @author sunny
 */

package com.prchny.base.concurrent;

import org.slf4j.MDC;

import com.prchny.base.utils.StringUtils;

public class ContextAwareRunnable implements Runnable {
  
  private Runnable runnable;
  
  private String requestId;
  
  public String getRequestId() {
  
    return requestId;
  }
  
  public void setRequestId(final String requestId) {
  
    this.requestId = requestId;
  }
  
  public Runnable getRunnable() {
  
    return runnable;
  }
  
  public void setRunnable(final Runnable runnable) {
  
    this.runnable = runnable;
  }
  
  @Override
  public void run() {
  
    if (StringUtils.isNotEmpty(requestId)) {
      MDC.put("requestId", requestId);
    }
    runnable.run();
  }
  
}
