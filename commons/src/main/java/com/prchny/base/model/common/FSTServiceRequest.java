/**
 * 
 */

package com.prchny.base.model.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fanendra
 *
 */
public class FSTServiceRequest extends ServiceRequest {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  protected static final AtomicLong requestId = new AtomicLong();
  
  private Object requestParam;
  
  private String requestTrackingId;
  
  private String requestAuthorizationKey;
  
  public String getRequestTrackingId() {
  
    return requestTrackingId;
  }
  
  public void setRequestTrackingId(final String requestTrackingId) {
  
    this.requestTrackingId = requestTrackingId;
  }
  
  public String getRequestAuthorizationKey() {
  
    return requestAuthorizationKey;
  }
  
  public void setRequestAuthorizationKey(final String requestAuthorizationKey) {
  
    this.requestAuthorizationKey = requestAuthorizationKey;
  }
  
  public Object getRequestParam() {
  
    return requestParam;
  }
  
  public void setRequestParam(final Object requestParam) {
  
    this.requestParam = requestParam;
  }
  
  public static AtomicLong getRequestid() {
  
    return requestId;
  }
  
}
