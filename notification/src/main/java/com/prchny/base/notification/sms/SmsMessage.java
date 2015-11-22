/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 21, 2010
 *  @author rahul
 */

package com.prchny.base.notification.sms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SmsMessage implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1995142201L;
  
  protected String mobile;
  
  protected String templateName;
  
  protected Map<String, Object> templateParams = new HashMap<String, Object>();
  
  protected String message;
  
  private boolean isDndActive;
  
  public SmsMessage(final String mobile) {
  
    this.mobile = mobile;
  }
  
  public SmsMessage(final String mobile, final String templateName) {
  
    this.mobile = mobile;
    this.templateName = templateName;
  }
  
  public String getMobile() {
  
    return mobile;
  }
  
  public void setMobile(final String mobile) {
  
    this.mobile = mobile;
  }
  
  public String getTemplateName() {
  
    return templateName;
  }
  
  public void setTemplateName(final String templateName) {
  
    this.templateName = templateName;
  }
  
  public Map<String, Object> getTemplateParams() {
  
    return templateParams;
  }
  
  public void addTemplateParam(final String name, final Object value) {
  
    templateParams.put(name, value);
  }
  
  public String getMessage() {
  
    return message;
  }
  
  public void setMessage(final String message) {
  
    this.message = message;
  }
  
  public boolean isDndActive() {
  
    return isDndActive;
  }
  
  public void setDndActive(final boolean isDndActive) {
  
    this.isDndActive = isDndActive;
  }
  
}
