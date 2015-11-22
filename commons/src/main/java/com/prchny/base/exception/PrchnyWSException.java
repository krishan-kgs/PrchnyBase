package com.prchny.base.exception;

/**
 * @version 1.0, 10-Apr-2014
 * @author hitesh
 */
public class PrchnyWSException extends Exception {
  
  private static final long serialVersionUID = 7036056880538287152L;
  
  /**
   * Depicts non 200 HTTP code Also has some special values depicting specials
   * cases See PrchnyWSErrorConstants
   */
  private int wsErrorCode;
  
  public PrchnyWSException() {
  
    super();
  }
  
  public PrchnyWSException(final String message, final Throwable cause) {
  
    super(message, cause);
  }
  
  public PrchnyWSException(final Integer wsErrorCode, final String message,
      final Throwable cause) {
  
    super(message, cause);
    this.wsErrorCode = wsErrorCode;
  }
  
  public int getWsErrorCode() {
  
    return wsErrorCode;
  }
  
  public void setWsErrorCode(final int wsErrorCode) {
  
    this.wsErrorCode = wsErrorCode;
  }
  
}
