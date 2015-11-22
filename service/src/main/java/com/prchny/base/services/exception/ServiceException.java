
package com.prchny.base.services.exception;

public class ServiceException extends Exception {
  
  public ServiceException(final String message) {
  
    super(message);
  }
  
  public ServiceException(final String message, final Throwable cause) {
  
    super(message, cause);
  }
  
  /**
   * 
   */
  private static final long serialVersionUID = -1790693060391411707L;
  
}
