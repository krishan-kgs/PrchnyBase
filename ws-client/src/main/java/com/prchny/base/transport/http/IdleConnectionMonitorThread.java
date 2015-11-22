/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 15-May-2012
 *  @author rahul
 */

package com.prchny.base.transport.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleConnectionMonitorThread extends Thread {
  
  private final ClientConnectionManager connMgr;
  
  private volatile boolean shutdown;
  
  private static final Logger LOG = LoggerFactory
      .getLogger(IdleConnectionMonitorThread.class);
  
  public IdleConnectionMonitorThread(final ClientConnectionManager connMgr) {
  
    super();
    this.connMgr = connMgr;
  }
  
  @Override
  public void run() {
  
    LOG.info("executed IdleConnectionMonitor");
    try {
      while (!shutdown) {
        synchronized (this) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("executed IdleConnectionMonitor for conn mgr : {}",
                connMgr.toString());
          }
          wait(5000);
          // Close expired connections
          connMgr.closeExpiredConnections();
          // Optionally, close connections
          // that have been idle longer than 60 sec
          connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
        }
      }
    } catch (final InterruptedException ex) {
      LOG.error("Error while closing expired/idle connections", ex);
    }
  }
  
  // call shutdown if you are calling shutdown on connection manager
  public void shutdown() {
  
    shutdown = true;
    synchronized (this) {
      notifyAll();
    }
  }
  
}
