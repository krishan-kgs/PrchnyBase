
package com.prchny.base.validation;

import java.io.Serializable;

import com.dyuproject.protostuff.Tag;

/**
 * @author singla
 */
public class ValidationError implements Serializable {
  
  private static final long serialVersionUID = 760859189756122L;
  
  @Tag(1)
  private int code;
  
  @Tag(2)
  private String message;
  
  public ValidationError() {
  
  }
  
  /**
   * @param code2
   * @param message2
   */
  public ValidationError(final int code, final String message) {
  
    this.code = code;
    this.message = message;
  }
  
  /**
   * @return the code
   */
  public int getCode() {
  
    return code;
  }
  
  /**
   * @param code
   *          the code to set
   */
  public void setCode(final int code) {
  
    this.code = code;
  }
  
  /**
   * @return the message
   */
  public String getMessage() {
  
    return message;
  }
  
  /**
   * @param message
   *          the message to set
   */
  public void setMessage(final String message) {
  
    this.message = message;
  }
}
