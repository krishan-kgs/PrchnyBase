/**
 * 
 */

package com.prchny.base.model.common;

/**
 * @author fanendra
 */
public class FSTServiceResponse extends ServiceResponse {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Throwable exception;
  
  private String responseTrackingId;
  
  public Throwable getException() {
  
    return exception;
  }
  
  public void setException(final Throwable exception) {
  
    this.exception = exception;
  }
  
  public String getResponseTrackingId() {
  
    return responseTrackingId;
  }
  
  public void setResponseTrackingId(final String responseTrackingId) {
  
    this.responseTrackingId = responseTrackingId;
  }
}
