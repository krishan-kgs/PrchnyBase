/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 19, 2010
 *  @author singla
 */

package com.prchny.base.transport.http;

/**
 * @author singla
 */
public class HttpTransportException extends Exception {
  
  private static final long serialVersionUID = -4896599313442168860L;
  
  /**
   * Code depicting standard HTTP response code and few more custom values
   * Custom codes having special meaning : -101 : IOException
   */
  private int code;
  
  /**
   * Constructs an <code>HttpTransportException</code> with the specified
   * message.
   * 
   * @param message
   * @param cause
   */
  public HttpTransportException(final String message, final Throwable cause) {
  
    super(message, cause);
  }
  
  /**
   * Constructs an <code>HttpTransportException</code> with the specified
   * message and root cause.
   * 
   * @param message
   */
  public HttpTransportException(final String message) {
  
    super(message);
  }
  
  /**
   * Constructs an <code>HttpTransportException</code> with the specified
   * message and HTTP response code.
   * 
   * @param message
   */
  public HttpTransportException(final String message, final int code) {
  
    super(message);
    setCode(code);
  }
  
  /**
   * Constructs an <code>HttpTransportException</code> with the specified cause
   * and HTTP response code.
   * 
   * @param message
   */
  public HttpTransportException(final Throwable cause, final int code) {
  
    super(cause);
    setCode(code);
  }
  
  public int getCode() {
  
    return code;
  }
  
  public void setCode(final int code) {
  
    this.code = code;
  }
}
