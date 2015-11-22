/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Nov 2, 2010
 *  @author rahul
 */

package com.prchny.base.notification.sms;

public class SmsMessageResponse {
  
  public enum SmsMessageResponseStatus {
    SUCCESS(
        "success"),
    ERROR(
        "error");
    
    private String status;
    
    private SmsMessageResponseStatus(final String status) {
    
      this.status = status;
    }
    
    public String status() {
    
      return status;
    }
  }
  
  private String code;
  
  private String message;
  
  private String status;
  
  public SmsMessageResponse() {
  
  }
  
  public SmsMessageResponse(final String code, final String message,
      final SmsMessageResponseStatus status) {
  
    this.code = code;
    this.message = message;
    this.status = status.status;
  }
  
  public String getCode() {
  
    return code;
  }
  
  public void setCode(final String code) {
  
    this.code = code;
  }
  
  public String getMessage() {
  
    return message;
  }
  
  public void setMessage(final String message) {
  
    this.message = message;
  }
  
  public String getStatus() {
  
    return status;
  }
  
  public void setStatus(final String status) {
  
    this.status = status;
  }
  
  @Override
  public String toString() {
  
    return "Gateway Response [code=" + code + ", message=" + message
        + ", status=" + status + "]";
  }
  
}
