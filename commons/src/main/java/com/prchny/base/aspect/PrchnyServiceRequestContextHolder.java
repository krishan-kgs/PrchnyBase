/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 03-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.aspect;

import com.prchny.base.services.request.context.LocalContextProvider;

/**
 * This class is modelled on SecurityContextHolder provided by spring security.<br>
 * Since we are enforcing inheritable thread local, we don't need a strategy
 * class as in spring.<br>
 * The purpose is to hold the RequestContext object for the Thread and provide
 * it to TransportService during further API calls<br>
 * <br>
 * There is no need to make any of the calls synchronized since the objects they
 * will be operating on are different, and there can be only one instruction the
 * stack of one thread, so one thread cannot simultaneously get and clear the
 * context.
 * 
 * @author ajinkya
 */
public class PrchnyServiceRequestContextHolder {
  
  private static final ThreadLocal<LocalContextProvider> contextHolder =
      new InheritableThreadLocal<LocalContextProvider>();
  
  /**
   * Please do not modify the context returned by this API. It will not result
   * in consistent logging.
   * 
   * @return
   */
  public static LocalContextProvider getRequestContext() {
  
    return contextHolder.get();
  }
  
  static void setRequestContext(final LocalContextProvider sro) {
  
    contextHolder.set(sro);
  }
  
  static void clearRequestContext() {
  
    contextHolder.remove();
  }
  
  public static String getLogString() {
  
    final LocalContextProvider sro = contextHolder.get();
    return sro == null ? null : sro.toString();
  }
  
}
